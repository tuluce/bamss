package net.bamss.bamss.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import net.bamss.bamss.connections.*;

import net.bamss.bamss.models.ShortenResult;
import net.bamss.bamss.models.Validation;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class UrlController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	private static final JedisPool jedisPool = RedisConnection.getRedisPool();
	private static final UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
	private static final int cacheSize = 4000; // %20 of the total url capacity, from 80/20 rule

	@PostMapping("/shorten")
	public ResponseEntity<ShortenResult> shorten(@RequestBody Map<String, String> body) {
		String token = body.get("token");
		String apiKey = body.get("api_key");
		String originalUrl = body.get("original_url");
		String customUrl = body.get("custom_url");
		String expDate = body.get("expire_date");

		// 1 month default expiration
		long defaultExpireDate = Instant.now().plusSeconds(2629743).toEpochMilli();
		long expireDate;
		if (expDate != null) {
			expireDate = Long.parseLong(expDate);
			if (expireDate > defaultExpireDate) {
				expireDate = defaultExpireDate;
			}
		} else {
			expireDate = defaultExpireDate; 
		}

		Validation validation = validate(token, apiKey);
		final String accountType = (token != null) ? "standard" : "business";

		if (validation != null) {
			if (validation.getQuota() > 0) {
				String creator = validation.getUsername();
				MongoCollection<Document> collection = db.getCollection("urls");


				String key;
				if(customUrl != null){
					Document checkCustomUrl = collection.find(Filters.eq("key", customUrl)).first();
					if (checkCustomUrl != null) {
						return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
					}
					key = customUrl;
				}
				else{
					key = KeygenConnection.getKey();
				}

				if(key == null){
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}

				Document newUrl = new Document()
						.append("url", originalUrl)
						.append("key", key)
						.append("creator", creator)
						.append("expireDate", expireDate)
						.append("disabled", 0);
				collection.insertOne(newUrl);

				CompletableFuture.runAsync(() -> {
					AnalyticsConnection.recordShorten(accountType);
				});

				Jedis jedis = jedisPool.getResource();

				if (jedis != null) {
					Long curCacheSize = jedis.dbSize();
					if(curCacheSize >= cacheSize){
						String keyToBeDeleted = jedis.randomKey();
						jedis.del(keyToBeDeleted);
					}
					jedis.set(key, originalUrl);
					jedis.close();
				}

				return new ResponseEntity<>(new ShortenResult(key, originalUrl, String.valueOf(expireDate)), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
			}
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/{key}")
	public ResponseEntity<Object> redirect(@PathVariable String key, HttpServletRequest request) throws URISyntaxException {
		String url = null;

		Jedis jedis = jedisPool.getResource();

		if (jedis != null) {
			url = jedis.get(key);
		}

		if (url == null) {
			MongoCollection<Document> collection = db.getCollection("urls");
			Document result = collection.find(Filters.eq("key", key)).first();
			if (result == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			if (String.valueOf(result.get("disabled")).equals("1")) {
				url = String.valueOf(result.get("url"));
			}
			
			if (jedis != null) {
				Long curCacheSize = jedis.dbSize();
				if(curCacheSize >= cacheSize){
					String keyToBeDeleted = jedis.randomKey();
					jedis.del(keyToBeDeleted);
				}
				jedis.set(key, url);
			}
		}

		if (jedis != null) {
			jedis.close();
		}

		ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
		String ipAddress = IpToRegionConnection.getIpAddress(request);
		String platform = agent.getDeviceCategory().getCategory().getName();
		String os = agent.getOperatingSystem().getName();
		String region = IpToRegionConnection.getRegion(ipAddress);
		CompletableFuture.runAsync(() -> {
			AnalyticsConnection.recordRedirect(key, platform, region, os);
		});

		URI uri = new URI(url);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/urls")
	public ResponseEntity<Object> urls(@RequestBody Map<String, String> body) {
		String token = body.get("token");
		String apiKey = body.get("api_key");


		Validation validation = validate(token, apiKey);

		if (validation != null) {
			ArrayList<ShortenResult> urls = new ArrayList<>();
			String username = validation.getUsername();


			MongoCollection<Document> collection = db.getCollection("urls");
			Document findQuery = new Document("creator", new Document("$eq",username));

			for (Document document : collection.find(findQuery)) {
				String key = String.valueOf(document.get("key"));
				String url = String.valueOf(document.get("url"));
				String expireDate = String.valueOf(document.get("expireDate"));

				ShortenResult userUrl = new ShortenResult(key, url, expireDate);
				urls.add(userUrl);
			}

			return new ResponseEntity<>(urls, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	public Validation validate(String token, String apiKey){

		Validation validation = null;
		if (token != null) {
			validation = AuthConnection.validateToken(token);
		} else if (apiKey != null) {
			validation = AuthConnection.validateApiKey(apiKey);
		}

		return validation;
	}
}
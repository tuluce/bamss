package net.bamss.bamss.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import net.bamss.bamss.connections.*;

import net.bamss.bamss.models.ShortenResult;
import net.bamss.bamss.models.Validation;
import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

	@PostMapping("/shorten")
	public ResponseEntity<ShortenResult> shorten(@RequestBody Map<String, String> body) {
		String token = body.get("token");
		String apiKey = body.get("api_key");
		String originalUrl = body.get("original_url");
		String customUrl = body.get("custom_url");
		String expDate = body.get("expire_date");

		long expireDate;
		if (expDate != null) {
			expireDate = Long.parseLong(expDate);
		} else {
			expireDate = Instant.now().getEpochSecond() + 99999999;
		}

		String key = (customUrl != null) ? customUrl : KeygenConnection.getKey();

		Validation validation = validate(token, apiKey);

		final String accountType = (token != null) ? "standart" : "business";

		if (validation != null) {
			if (validation.getQuota() > 0) {
				String creator = validation.getUsername();
				MongoCollection<Document> collection = db.getCollection("urls");
				Document newUrl = new Document()
						.append("url", originalUrl)
						.append("key", key)
						.append("creator", creator)
						.append("expireDate", expireDate);
				collection.insertOne(newUrl);

				CompletableFuture.runAsync(() -> {
					AnalyticsConnection.recordShorten(accountType);
				});

				return new ResponseEntity<>(new ShortenResult(key, originalUrl), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
			}
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/{key}")
	public ResponseEntity<Object> redirect(@PathVariable String key) throws URISyntaxException {
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

			url = String.valueOf(result.get("url"));

			if (jedis != null) {
				jedis.set(key, url);
			}
		}

		if (jedis != null) {
			jedis.close();
		}

		CompletableFuture.runAsync(() -> {
			AnalyticsConnection.recordRedirect(key, "mobile", "TR", "android");
		});

		URI uri = new URI("https://www." + url);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@GetMapping("/urls")
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

				ShortenResult userUrl = new ShortenResult(key, url);
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
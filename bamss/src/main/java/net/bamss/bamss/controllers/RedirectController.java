package net.bamss.bamss.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import net.bamss.bamss.connections.AnalyticsConnection;
import net.bamss.bamss.connections.MongoConnection;

import net.bamss.bamss.connections.RedisConnection;
import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@RestController
public class RedirectController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	private static final JedisPool jedisPool = RedisConnection.getRedisPool();

	@GetMapping("/{key}")
	public ResponseEntity<Object> redirect(@PathVariable String key) throws URISyntaxException {
		String url = null;

		Jedis jedis = jedisPool.getResource();

		if(jedis != null){
			url = jedis.get(key);
		}

		if(url == null){
			MongoCollection<Document> collection = db.getCollection("urls");
			Document result = collection.find(Filters.eq("key", key)).first();
			if (result == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			url = String.valueOf(result.get("url"));

			if(jedis != null){
				jedis.set(key,url);
			}
		}

		if(jedis != null){
			jedis.close();
		}

		CompletableFuture.runAsync(() -> {
			AnalyticsConnection.recordRedirect(key, "mobile", "TR", "android");
		});

		URI uri = new URI(url);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
}

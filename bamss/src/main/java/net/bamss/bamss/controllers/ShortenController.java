package net.bamss.bamss.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.bamss.bamss.connections.AnalyticsConnection;
import net.bamss.bamss.connections.AuthConnection;
import net.bamss.bamss.connections.KeygenConnection;
import net.bamss.bamss.connections.MongoConnection;
import net.bamss.bamss.models.ShortenResult;
import net.bamss.bamss.models.Validation;

import org.bson.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ShortenController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();

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
		}else{
			expireDate = Instant.now().getEpochSecond() + 99999999;
		}

		String key = (customUrl != null) ? customUrl : KeygenConnection.getKey();

		Validation validation = null;
		if (token != null) {
			validation = AuthConnection.validateToken(token);
		} else if (apiKey != null) {
			validation = AuthConnection.validateApiKey(apiKey);
		}
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
				
				return new ResponseEntity<>(new ShortenResult(key), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
			}
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

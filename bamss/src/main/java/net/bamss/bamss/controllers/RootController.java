package net.bamss.bamss.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.bamss.bamss.connections.MongoConnection;
import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

@RestController
public class RootController {

	private static final MongoDatabase db = MongoConnection.getMongoDatabase();

	@PostMapping("/shorten")
	public ResponseEntity shorten(@RequestBody Map<String,String> body) {
		String authToken = body.get("auth_token");
		String apiKey = body.get("api_key");
		String originalUrl = body.get("original_key");
		String customUrl = body.get("custom_url");
		String expDate = body.get("expire_date");

		long expireDate;
		if (expDate != null) {
			expireDate = Long.parseLong(expDate);
		}else{
			expireDate = Instant.now().getEpochSecond() + 99999999;
		}

		if (customUrl == null){
			Random rand = new Random();
			customUrl = Integer.toString(rand.nextInt(999999));
		}

		MongoCollection<Document> collection = db.getCollection("urls");

		Document newUrl = new Document("url", originalUrl)
				.append("key", customUrl)
				.append("expireDate", expireDate);

		collection.insertOne(newUrl);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping("/{key}")
	public ResponseEntity redirect(@PathVariable String key) throws URISyntaxException {
		MongoCollection<Document> collection = db.getCollection("urls");
		Document result = collection.find(eq("key", key)).first();
		if (result == null){
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		String url = String.valueOf(result.get("url"));

		URI uri = new URI("https://www." + url);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

	}
}

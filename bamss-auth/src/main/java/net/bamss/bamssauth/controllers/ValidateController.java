package net.bamss.bamssauth.controllers;

import java.util.Map;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.connections.MongoConnection;
import net.bamss.bamssauth.models.Validation;

@RestController
public class ValidateController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	
	@PostMapping("/validate")
	public ResponseEntity<Validation> validate(@RequestBody Map<String,String> body) {
		String token = body.get("token");
		String apiKey = body.get("api_key");

		if ((token == null && apiKey == null) || (token != null && apiKey != null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		MongoCollection<Document> collection = db.getCollection("user");

		if (token != null) {
			Document result = collection.find(Filters.eq("username", token)).first();
			if (result != null) {
				return new ResponseEntity<>(new Validation(token), HttpStatus.OK);
			}
		} else {
			Document result = collection.find(Filters.eq("username", apiKey)).first();
			if (result != null) {
				return new ResponseEntity<>(new Validation(apiKey), HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

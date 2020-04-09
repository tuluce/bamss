package net.bamss.bamssauth.controllers;

import java.util.Map;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.connections.MongoConnection;;

@RestController
public class SignupController {
	
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	
	@PutMapping("/user")
	public ResponseEntity<Object> signup(@RequestBody Map<String,String> body) {
		String email = body.get("email");
		String username = body.get("username");
		String password = body.get("password");
		String accountType = body.get("account_type");

		MongoCollection<Document> collection = db.getCollection("user");

		Document newUser = new Document()
			.append("email", email)
			.append("username", username)
			.append("password", password)
			.append("account_type", accountType);

		collection.insertOne(newUser);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}

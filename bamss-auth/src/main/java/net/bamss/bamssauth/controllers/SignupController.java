package net.bamss.bamssauth.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.connections.MongoConnection;

@RestController
public class SignupController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	
	@PutMapping("/user")
	public ResponseEntity<Object> signup(@RequestBody Map<String,String> body) {
		String email = body.get("email");
		String username = body.get("username");
		String password = body.get("password");
		String accountType = body.get("account_type");
		String passwordHash = DigestUtils.sha256Hex(password);
		MongoCollection<Document> collection = db.getCollection("user");
		Document newUser = new Document()
			.append("email", email)
			.append("username", username)
			.append("password_hash", passwordHash)
			.append("account_type", accountType)
			.append("url_creations", new ArrayList<Date>());
		collection.insertOne(newUser);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}

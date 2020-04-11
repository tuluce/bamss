package net.bamss.bamssauth.controllers;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.connections.AnalyticsConnection;
import net.bamss.bamssauth.connections.MongoConnection;
import net.bamss.bamssauth.models.QuotaStatus;
import net.bamss.bamssauth.models.Signup;

@RestController
public class SignupController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	
	@PutMapping("/user")
	public ResponseEntity<Signup> signup(@RequestBody Map<String,String> body) {
		String email = body.get("email");
		String username = body.get("username");
		String password = body.get("password");
		String accountType = body.get("account_type");
		String passwordHash = DigestUtils.sha256Hex(password);
		MongoCollection<Document> collection = db.getCollection("user");
		Document prevUsername = collection.find(Filters.eq("username", username)).first();
		Document prevEmail = collection.find(Filters.eq("email", email)).first();
		if (prevUsername != null) {
			return new ResponseEntity<>(new Signup("Username already in use."), HttpStatus.CONFLICT);
		}
		if (prevEmail != null) {
			return new ResponseEntity<>(new Signup("Email already in use."), HttpStatus.CONFLICT);
		}
		if (username.length() < 3 || 64 < username.length()) {
			return new ResponseEntity<>(new Signup("Username length should be in range [3 - 64]."), 
				HttpStatus.BAD_REQUEST);
		}
		if (password.length() < 3 || 64 < password.length()) {
			return new ResponseEntity<>(new Signup("Password length should be in range [3 - 64]."), 
				HttpStatus.BAD_REQUEST);
		}
		if (email.length() < 8 || 512 < email.length()) {
			return new ResponseEntity<>(new Signup("Email length should be in range [8 - 512]."), 
				HttpStatus.BAD_REQUEST);
		}
		Document newUser = new Document()
			.append("email", email)
			.append("username", username)
			.append("password_hash", passwordHash)
			.append("account_type", accountType)
			.append("quota_status", new QuotaStatus(0, null).getDocument());
		collection.insertOne(newUser);
		CompletableFuture.runAsync(() -> {
			AnalyticsConnection.recordSignup(accountType);
		});
		return new ResponseEntity<>(new Signup(), HttpStatus.CREATED);
	}
}

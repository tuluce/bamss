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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.models.BusinessAuth;
import net.bamss.bamssauth.models.BaseAuth;
import net.bamss.bamssauth.models.StandartAuth;
import net.bamss.bamssauth.util.AuthUtils;
import net.bamss.bamssauth.connections.AnalyticsConnection;
import net.bamss.bamssauth.connections.MongoConnection;

@RestController
public class LoginController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	
	@PostMapping("/user")
	public ResponseEntity<BaseAuth> login(@RequestBody Map<String,String> body) {
		String username = body.get("username");
    String password = body.get("password");
    String passwordHash = DigestUtils.sha256Hex(password);

    MongoCollection<Document> collection = db.getCollection("user");
    Document result = collection.find(Filters.eq("username", username)).first();
		if (result == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    String realPasswordHash = String.valueOf(result.get("password_hash"));
    if (realPasswordHash.equals(passwordHash)) {
      String accountType = String.valueOf(result.get("account_type"));
      CompletableFuture.runAsync(() -> {
        AnalyticsConnection.recordLogin(accountType);
      });
      if (accountType.equals("standart")) {
        String token = AuthUtils.getToken(username);
        return new ResponseEntity<>(new StandartAuth(token), HttpStatus.OK);
      } else if (accountType.equals("business")) {
        String apiKey = AuthUtils.getApiKey(username);
        return new ResponseEntity<>(new BusinessAuth(apiKey), HttpStatus.OK);
      }
    }

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

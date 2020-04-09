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

import net.bamss.bamssauth.models.BusinessAuth;
import net.bamss.bamssauth.models.BaseAuth;
import net.bamss.bamssauth.models.StandartAuth;
import net.bamss.bamssauth.connections.MongoConnection;

@RestController
public class LoginController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();
	
	@PostMapping("/user")
	public ResponseEntity<BaseAuth> login(@RequestBody Map<String,String> body) {
		String username = body.get("username");
		String password = body.get("password");

    MongoCollection<Document> collection = db.getCollection("user");
    Document result = collection.find(Filters.eq("username", username)).first();
		if (result == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    String realPassword = String.valueOf(result.get("password"));
    if (realPassword.equals(password)) {
      String accountType = String.valueOf(result.get("account_type"));
      if (accountType.equals("standart")) {
        return new ResponseEntity<>(new StandartAuth("TokenContentHere"), HttpStatus.OK);
      } else if (accountType.equals("business")) {
        return new ResponseEntity<>(new BusinessAuth("ApiKeyContentHere"), HttpStatus.OK);
      }
      
    }

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

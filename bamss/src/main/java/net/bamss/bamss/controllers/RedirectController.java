package net.bamss.bamss.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import net.bamss.bamss.connections.MongoConnection;

import org.bson.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class RedirectController {
	private static final MongoDatabase db = MongoConnection.getMongoDatabase();

	@GetMapping("/{key}")
	public ResponseEntity<Object> redirect(@PathVariable String key) throws URISyntaxException {
		MongoCollection<Document> collection = db.getCollection("urls");
		Document result = collection.find(Filters.eq("key", key)).first();
		if (result == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String url = String.valueOf(result.get("url"));

		URI uri = new URI(url);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
}

package net.bamss.bamssauth.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.models.Validation;
import net.bamss.bamssauth.util.AuthUtils;

@RestController
public class ValidateController {
	@PostMapping("/validate")
	public ResponseEntity<Validation> validate(@RequestBody Map<String,String> body) {
		String token = body.get("token");
		String apiKey = body.get("api_key");

		if ((token == null && apiKey == null) || (token != null && apiKey != null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (token != null) {
			String username = AuthUtils.validateToken(token);
			if (username != null) {
				return new ResponseEntity<>(new Validation(username), HttpStatus.OK);
			}
		} else {
			String username = AuthUtils.validateApiKey(apiKey);
			if (username != null) {
				return new ResponseEntity<>(new Validation(username), HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

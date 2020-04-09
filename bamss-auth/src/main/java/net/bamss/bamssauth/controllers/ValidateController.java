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
		
		String username;
		if (token != null) {
			username = AuthUtils.validateToken(token);
		} else {
			username = AuthUtils.validateApiKey(apiKey);
		}
		if (username != null) {
			boolean hasQuota = AuthUtils.checkQuota(username);
			return new ResponseEntity<>(new Validation(username, hasQuota), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

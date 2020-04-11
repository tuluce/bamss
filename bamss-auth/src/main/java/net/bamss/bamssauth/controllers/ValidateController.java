package net.bamss.bamssauth.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssauth.models.Validation;
import net.bamss.bamssauth.util.AuthUtils;
import net.bamss.bamssauth.util.QuotaUtils;

@RestController
public class ValidateController {
	@PostMapping("/validate")
	public ResponseEntity<Validation> validate(@RequestBody Map<String, Object> body) {
		String token = (String) body.get("token");
		String apiKey = (String) body.get("api_key");
		String adminKey = (String) body.get("admin_key");
		Boolean useQuota = (Boolean) body.get("use_quota");
		
		if (useQuota == null) {
			useQuota = false;
		}

		if (token == null && apiKey == null && adminKey == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		String username = null;
		if (token != null) {
			username = AuthUtils.validateToken(token);
		} else if (apiKey != null) {
			username = AuthUtils.validateApiKey(apiKey);
		} else if (adminKey != null) {
			if (AuthUtils.validateAdminKey(adminKey)) {
				return new ResponseEntity<>(new Validation("admin", 1), HttpStatus.OK);
			}
		}
		if (username != null) {
			int quota = QuotaUtils.getQuota(username);
			if (quota > 0 && useQuota) {
				QuotaUtils.useQuota(username);
			}
			return new ResponseEntity<>(new Validation(username, quota), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

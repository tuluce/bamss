package net.bamss.bamssanalytics.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	@PostMapping("/admin")
	public ResponseEntity<Object> adminLevelAnalytis(@RequestBody Map<String, Object> body) {
    String adminKey = (String) body.get("admin_key");
    String eventType = (String) body.get("event_type");
    String startDate = (String) body.get("start_date");
    String endDate = (String) body.get("end_date");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

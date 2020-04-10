package net.bamss.bamssanalytics.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {
	@PostMapping("/analytics")
	public ResponseEntity<Object> userLevelAnalytis(@RequestBody Map<String, Object> body) {
    String token = (String) body.get("token");
    String apiKey = (String) body.get("api_key");
    String startDate = (String) body.get("start_date");
    String endDate = (String) body.get("end_date");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

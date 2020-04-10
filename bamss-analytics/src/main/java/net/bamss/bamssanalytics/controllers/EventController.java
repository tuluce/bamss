package net.bamss.bamssanalytics.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssanalytics.util.DatabaseUtils;

@RestController
public class EventController {
	@PostMapping("/event")
	public ResponseEntity<Object> recordEvent(@RequestBody Map<String, Object> body) {
		String eventType = (String) body.get("event_type");
    String accountType = (String) body.get("account_type");
    if (eventType.equals("redirect")) {
      String key = (String) body.get("key");
      String platform = (String) body.get("platform");
      String locale = (String) body.get("locale");
      String os = (String) body.get("os");
      DatabaseUtils.insertEvent(eventType, accountType, key, platform, locale, os);
    } else {
      DatabaseUtils.insertEvent(eventType, accountType);
    }
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

package net.bamss.bamssanalytics.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamssanalytics.connections.AuthConnection;
import net.bamss.bamssanalytics.models.AdminAnalytics;
import net.bamss.bamssanalytics.util.DatabaseUtils;

@RestController
public class AdminController {
	@PostMapping("/admin")
	public ResponseEntity<AdminAnalytics> adminLevelAnalytis(@RequestBody Map<String, Object> body) {
    String adminKey = (String) body.get("admin_key");
    long startDateTs = Long.parseLong("" + body.get("start_date"));
    long endDateTs = Long.parseLong("" + body.get("end_date"));
    long resolution = Long.parseLong("" + body.get("resolution"));

    if (AuthConnection.validateAdminKey(adminKey)) {
      AdminAnalytics result = DatabaseUtils.getAdminLevelAnalytics(startDateTs, endDateTs, resolution);
      return new ResponseEntity<>(result, HttpStatus.OK);
    }

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}

package net.bamss.bamssauth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping(value = "/health", produces = "application/json")
	public String root() {
		return "{\"status\":\"up\"}";
	}
}

package net.bamss.bamssanalytics.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
	@GetMapping("/")
	public String root() {
		return "Bamss Analytics API Root";
	}
}

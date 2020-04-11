package net.bamss.bamss.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
	@GetMapping("/")
	public String root() {
		return "Bamss API Root";
	}
}

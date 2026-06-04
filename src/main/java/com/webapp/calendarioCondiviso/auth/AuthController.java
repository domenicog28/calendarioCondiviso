package com.webapp.calendarioCondiviso.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@PostMapping( path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody Credenziali credenziali){
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/*
	@PostMapping( path = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refresh(@RequestBody Credenziali credenziali){
		
		return new ResponseEntity<>(HttpStatus.OK);
	}*/

}

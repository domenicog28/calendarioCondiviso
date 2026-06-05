package com.webapp.calendarioCondiviso.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthServiceImpl authService;
	
	@PostMapping( path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody Credenziali credenziali){
		
		String[] token = authService.login(credenziali);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION,"Bearer " + token[0])
				.header("X-Refresh-Token", token[1])
				.body("Login effettuato con successo!");
	}
	
	
	@PostMapping( path = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refresh(@RequestHeader("X-Refresh-Token") String refreshToken){
		
		String accessToken = authService.refresh(refreshToken);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
				.body("Refresh eseguito con successo!");
	}

}

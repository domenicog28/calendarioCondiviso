package com.webapp.calendarioCondiviso.email;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class EmailController {
	
	@Autowired
	EmailSenderServiceImpl emailService;
	
	
	
	@PostMapping ( path = "/verifica-email", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verificaEmail(@RequestBody VerificaEmailDto verificaDto){
		
		String[] token = emailService.modificaVerificaEmail(verificaDto.email(), verificaDto.codiceVerifica());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION,"Bearer " + token[0])
				.header("X-Refresh-Token", token[1])
				.body("Login effettuato con successo!");
		
	}
	
	@PostMapping ( path = "/verifica-email/nuovoCodice", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> nuovoCodice (@RequestBody Map<String, Object> body){
		
		String email = (String) body.get("email");
		
		System.out.println();
		
		Boolean inviato = emailService.sendNewToken(email);
		
		if (inviato) {
			
			return ResponseEntity.ok().body("Il nuovo codice di verifica è stato inviato");
			
		}
		
		return ResponseEntity.internalServerError().build();
		
	}
	

}

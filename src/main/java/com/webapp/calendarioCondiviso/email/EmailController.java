package com.webapp.calendarioCondiviso.email;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
	/*@PostMapping ( path = "/verifica-email", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verificaEmail(@RequestBody String codiceVerifica, String email){
		
		String[] Token = emailService.modificaVerificaEmail(email, codiceVerifica);
		
	}*/

}

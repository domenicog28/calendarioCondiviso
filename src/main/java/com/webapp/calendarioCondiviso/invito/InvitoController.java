package com.webapp.calendarioCondiviso.invito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.calendarioCondiviso.invito.dto.InvitoCreateDTO;

@RestController
@RequestMapping("/inviti")
public class InvitoController {
	
	@Autowired
	InvitoService invitoService;
	
	@PostMapping (path = "/registrazione", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registrazioneInvito (@RequestBody InvitoCreateDTO createDto){
		
		invitoService.inserisciInvito(createDto);
		
		return new ResponseEntity<>("Invito creato con successo!", HttpStatus.CREATED);
		
	}

}

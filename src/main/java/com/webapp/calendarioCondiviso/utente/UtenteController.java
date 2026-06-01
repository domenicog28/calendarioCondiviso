package com.webapp.calendarioCondiviso.utente;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteUpdateDTO;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
	
	@Autowired
	UtenteService utenteService;
	
	@PostMapping(path = "/registrazione/{uuidOrganizzazione}", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> registrazioneUtente (@PathVariable UUID uuidOrganizzazione, @RequestBody UtenteCreateDTO createDto) {
		
		utenteService.inserisciUtente(uuidOrganizzazione, createDto);
		
		return new ResponseEntity<>("Utente registrato con successo!", HttpStatus.CREATED);
		
	}
	
	@PutMapping (path = "/modifica/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> modificaUtente (@PathVariable UUID uuid, @RequestBody UtenteUpdateDTO updateDto) {
		
		utenteService.modificaUtente(uuid, updateDto);
		
		return new ResponseEntity<>("Utente modificato con successo", HttpStatus.OK);
		
	}
	
	@DeleteMapping ("/elimina/{uuid}")
	private ResponseEntity<?> eliminaUtente (@PathVariable UUID uuid) {
		
		utenteService.eliminaUtente(uuid);
		
		return new ResponseEntity<>("Utente eliminato con successo", HttpStatus.NO_CONTENT);
		
	}
	
	@GetMapping("/{uuid}/eventi")
	private ResponseEntity<?> eventiUtente (@PathVariable UUID uuid) {
		
		List<EventoResponseDTO> eventi = utenteService.cercaEventiUtente(uuid);
		
		return new ResponseEntity<>(eventi, HttpStatus.OK);
		
	}
	
	@GetMapping("/{uuid}")
	private ResponseEntity<?> dettagliUtente (@PathVariable UUID uuid) {
		
		UtenteResponseDTO utente = utenteService.cercaPerUUID(uuid);
		
		return new ResponseEntity<>(utente, HttpStatus.OK);
		
	}

}

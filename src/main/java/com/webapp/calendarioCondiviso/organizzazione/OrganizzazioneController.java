package com.webapp.calendarioCondiviso.organizzazione;

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
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneUpdateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

@RestController
@RequestMapping("/organizzazioni")
public class OrganizzazioneController {
	
	@Autowired
	OrganizzazioneService organizzazioneService;
	
	@PostMapping(path = "/registrazione", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registrazioneOrganizzazione(@RequestBody OrganizzazioneCreateDTO createDto) {
		
		organizzazioneService.inserisciOrganizzazione(createDto);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/modifica/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificaOrganizzazione (@PathVariable UUID uuid, @RequestBody OrganizzazioneUpdateDTO updateDto){
		
		organizzazioneService.modificaOrganizzazione(uuid, updateDto);
		
		return new ResponseEntity<>("Organizzazione modificata con successo!", HttpStatus.OK);
		
	}
	
	@DeleteMapping(path = "/elimina/{uuid}")
	public ResponseEntity<?> eliminaOrganizzazione (@PathVariable UUID uuid){
		
		organizzazioneService.eliminaOrganizzazione(uuid);
		
		return new ResponseEntity<>("Organizzazione eliminata con successo!", HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{uuid}/utenti")
	public ResponseEntity<?> utentiOrganizzazione (@PathVariable UUID uuid) {
		
		List<UtenteResponseDTO> utenti = organizzazioneService.cercaUtentiOrganizzazione(uuid);
		
		return new ResponseEntity<>(utenti, HttpStatus.OK);
		
	}
	
	@GetMapping("/{uuid}/eventi")
	public ResponseEntity<?> eventiOrganizzazione (@PathVariable UUID uuid) {
		
		List<EventoResponseDTO> eventi = organizzazioneService.cercaEventiOrganizzazione(uuid);
		
		return new ResponseEntity<> (eventi, HttpStatus.OK);
		
	}
	
	@GetMapping("/{uuid}")
	public ResponseEntity<?> dettagliOrganizzazione (@PathVariable UUID uuid) {
		
		OrganizzazioneResponseDTO responseDto = organizzazioneService.cercaPerUUID(uuid);
		
		return new ResponseEntity<> (responseDto, HttpStatus.OK);
		
	}
	
}

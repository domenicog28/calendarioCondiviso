package com.webapp.calendarioCondiviso.evento;

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

import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoUpdateDTO;

@RestController
@RequestMapping("/eventi")
public class EventoController {
	
	@Autowired
	EventoService eventoService;
	
	@PostMapping (path = "/registrazione", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registrazioneEvento (@RequestBody EventoCreateDTO createDto) {
		
		eventoService.inserisciEvento(createDto);
		
		return new ResponseEntity<>("Evento creato con successo!", HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/modifica/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificaEvento (@PathVariable UUID uuid, @RequestBody EventoUpdateDTO updateDto) {
		
		eventoService.modificaEvento(uuid, updateDto);
		
		return new ResponseEntity<>("Evento modificato con successo!", HttpStatus.OK);
		
	}
	
	@DeleteMapping ("/elimina/{uuid}")
	public ResponseEntity<?> eliminaEvento (@PathVariable UUID uuid){
		
		eventoService.eliminaEvento(uuid);
		
		return new ResponseEntity<>("Evento eliminato con successo!", HttpStatus.NO_CONTENT);
		
	}
	
	@GetMapping("/{uuid}")
	public ResponseEntity<?> dettagliEvento(@PathVariable UUID uuid){
		
		EventoResponseDTO evento = eventoService.cercaPerId(uuid);
		
		return new ResponseEntity<>(evento, HttpStatus.OK);
		
	}
	
}

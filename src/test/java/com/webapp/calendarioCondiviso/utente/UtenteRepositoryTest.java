package com.webapp.calendarioCondiviso.utente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
@Testcontainers
public class UtenteRepositoryTest {
	
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
	
	ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Autowired
	OrganizzazioneRepository organizzazioneRepository;
	
	@BeforeEach
	void creazioneUtente() {
		Organizzazione org = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		Organizzazione orgSalvata = organizzazioneRepository.save(org);
		
		Utente utente = Utente.builder().organizzazione(orgSalvata).email("utente@test.it").passwordHash("123Ciao!").nome("Marco")
				.cognome("Rossi").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		utenteRepository.save(utente);
	}
	
	@Test
	@Order(1)
	void ricercaPerEmail() {
		Utente cercato = utenteRepository.findByEmail("utente@test.it")
				.orElseThrow(()-> new RuntimeException("Utente non trovato"));
		assertEquals("utente@test.it", cercato.getEmail());
		assertEquals("123Ciao!", cercato.getPasswordHash());
		assertEquals("Marco", cercato.getNome());
		assertEquals(123456, cercato.getTokenVerifica());
		assertEquals("2026-05-02T14:30+02:00[Europe/Rome]", cercato.getScadenzaToken().toString());
		assertFalse(cercato.isVerificato());
	}
	
	@Test
	@Order(2)
	void ricercaPerToken() {

		Utente cercato = utenteRepository.findByTokenVerifica(123456)
				.orElseThrow(()-> new RuntimeException("Utente non trovato"));
		
		assertEquals(123456, cercato.getTokenVerifica());
		
	}
	
	@Test
	@Order(3)
	void emailDuplicata() {
		Organizzazione organizzazione = organizzazioneRepository.findByEmail("prova@test.it")
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));
		
		Utente utente2 = Utente.builder().organizzazione(organizzazione).email("utente@test.it").passwordHash("123Ciao!").nome("Marco")
				.cognome("Rossi").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		assertThrows(DataIntegrityViolationException.class, () -> utenteRepository.saveAndFlush(utente2));
	}
	
	@Test
	@Order(4)
	void ricercaEmailInesistente() {
		String email = "inesistente@test.it";

		assertTrue(utenteRepository.findByEmail(email).isEmpty());
	}
	
}

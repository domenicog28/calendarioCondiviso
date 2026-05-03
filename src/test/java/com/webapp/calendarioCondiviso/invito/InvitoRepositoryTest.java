package com.webapp.calendarioCondiviso.invito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class InvitoRepositoryTest {
	
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
	
	ZonedDateTime time = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));
	ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 3, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

	@Autowired
	OrganizzazioneRepository organizzazioneRepository;
	
	@Autowired
	InvitoRepository invitoRepository;
	
	@BeforeEach
	void creazioneOrganizzazione() {
		Organizzazione org = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		Organizzazione organizzazioneSalvata = organizzazioneRepository.save(org);
		
		Invito invito = Invito.builder().organizzazione(organizzazioneSalvata).timeGenerato(time).scadenzaToken(scadenza).build();
		
		invitoRepository.save(invito);
	}
	
	@Test
	void ricercaPerOrganizzazione() {
		Organizzazione cercata = organizzazioneRepository.findByEmail("prova@test.it")
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));
		
		List<Invito> inviti = invitoRepository.findByOrganizzazione(cercata);
		
		assertEquals(1, inviti.size());
	}
	
	@Test
	void ricercaPerToken() {
		Organizzazione cercata = organizzazioneRepository.findByEmail("prova@test.it")
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));
		
		List<Invito> inviti = invitoRepository.findByOrganizzazione(cercata);
		
		Invito invito = invitoRepository.findById(inviti.get(0).getIdToken())
				.orElseThrow(() -> new RuntimeException("Invito non trovato"));
		
		assertEquals(time.toString(), invito.getTimeGenerato().toString());
		assertEquals(scadenza.toString(), invito.getScadenzaToken().toString());
	}
}

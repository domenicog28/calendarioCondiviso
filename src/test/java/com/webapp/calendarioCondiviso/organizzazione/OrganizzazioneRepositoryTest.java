package com.webapp.calendarioCondiviso.organizzazione;

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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class OrganizzazioneRepositoryTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

	ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

	@Autowired
	OrganizzazioneRepository organizzazioneRepository;

	@BeforeEach
	void creazioneOrganizzazione() {
		Organizzazione org = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();

		organizzazioneRepository.save(org);
	}

	@Test
	@Order(1)
	void ricercaPerEmail() {

		Organizzazione cercata = organizzazioneRepository.findByEmail("prova@test.it")
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));
		assertEquals("prova@test.it", cercata.getEmail());
		assertEquals("123Ciao!", cercata.getPasswordHash());
		assertEquals("SquadraCalcio", cercata.getDescrizione());
		assertEquals(123456, cercata.getTokenVerifica());
		assertEquals("2026-05-02T14:30+02:00[Europe/Rome]", cercata.getScadenzaToken().toString());
		assertFalse(cercata.isVerificata());

	}

	@Test
	@Order(2)
	void ricercaPerToken() {

		Organizzazione cercata = organizzazioneRepository.findByTokenVerifica(123456)
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));

		assertEquals(123456, cercata.getTokenVerifica());

	}

	@Test
	@Order(3)
	void emailDuplicata() {
		Organizzazione org2 = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();

		assertThrows(DataIntegrityViolationException.class, () -> organizzazioneRepository.saveAndFlush(org2));

	}

	@Test
	@Order(4)
	void ricercaEmailInesistente() {
		String email = "inesistente@test.it";

		assertTrue(organizzazioneRepository.findByEmail(email).isEmpty());
	}

}

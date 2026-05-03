package com.webapp.calendarioCondiviso.evento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class EventoRepositoryTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

	ZonedDateTime time = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

	@Autowired
	UtenteRepository utenteRepository;

	@Autowired
	OrganizzazioneRepository organizzazioneRepository;

	@Autowired
	EventoRepository eventoRepository;

	@BeforeEach
	void creazioneEvento() {
		Organizzazione org = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(time).build();

		Organizzazione orgSalvata = organizzazioneRepository.save(org);

		Utente utente = Utente.builder().organizzazione(orgSalvata).email("utente@test.it").passwordHash("123Ciao!")
				.nome("Marco").cognome("Rossi").tokenVerifica(123456).scadenzaToken(time).build();

		Utente utenteSalvato = utenteRepository.save(utente);

		Evento evento = Evento.builder().organizzazione(orgSalvata).utente(utenteSalvato).tipo(TipoEvento.PERSONALE)
				.titolo("appuntamento").descrizione("lavoro").timeInizio(time).timeFine(time).timeNotifica(time)
				.build();

		eventoRepository.save(evento);

		Evento evento2 = Evento.builder().organizzazione(orgSalvata).tipo(TipoEvento.TEAM).titolo("appuntamento")
				.descrizione("lavoro").timeInizio(time).timeFine(time).timeNotifica(time).build();

		eventoRepository.save(evento2);
	}

	@Test
	@Order(1)
	void ricercaPerOrganizzazione() {
		Organizzazione organizzazione = organizzazioneRepository.findByEmail("prova@test.it")
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));

		List<Evento> eventi = eventoRepository.findByOrganizzazione(organizzazione);

		assertEquals(2, eventi.size());

	}

	@Test
	@Order(2)
	void ricercaPerUtente() {
		Utente utente = utenteRepository.findByEmail("utente@test.it")
				.orElseThrow(() -> new RuntimeException("Utente non trovato"));

		List<Evento> eventi = eventoRepository.findByUtente(utente);

		assertEquals(1, eventi.size());

		assertEquals("PERSONALE", eventi.get(0).getTipo().toString());
		assertEquals("appuntamento", eventi.get(0).getTitolo());
		assertEquals("lavoro", eventi.get(0).getDescrizione());
		assertEquals("2026-05-02T14:30+02:00[Europe/Rome]", eventi.get(0).getTimeInizio().toString());
		assertEquals("2026-05-02T14:30+02:00[Europe/Rome]", eventi.get(0).getTimeFine().toString());
		assertEquals("2026-05-02T14:30+02:00[Europe/Rome]", eventi.get(0).getTimeNotifica().toString());
	}

	@Test
	@Order(3)
	void ricercaEventiTeam() {
		Organizzazione organizzazione = organizzazioneRepository.findByEmail("prova@test.it")
				.orElseThrow(() -> new RuntimeException("Organizzazione non trovata"));

		List<Evento> eventi = eventoRepository.findByOrganizzazioneAndTipo(organizzazione, TipoEvento.TEAM);
		
		assertEquals(1, eventi.size());
	}
	
	@Test
	@Order(4)
	void ricercaPerDataNotifica() {
		
		List<Evento> eventi = eventoRepository.findByTimeNotificaBeforeAndNotificatoFalse(time.plusMinutes(1));
		
		assertEquals(2, eventi.size());
	}
}

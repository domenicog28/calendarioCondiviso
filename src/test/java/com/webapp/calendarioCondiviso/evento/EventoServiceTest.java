package com.webapp.calendarioCondiviso.evento;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoUpdateDTO;
import com.webapp.calendarioCondiviso.exception.EventoNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {
	
	@Mock
	OrganizzazioneRepository organizzazioneRepository;
	
	@Mock
	UtenteRepository utenteRepository;

	@Mock
	EventoMapper eventoMapper;

	@Mock
	EventoRepository eventoRepository;

	@InjectMocks
	EventoServiceImpl eventoServiceImpl;

	ZonedDateTime time = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

	private Organizzazione organizzazione;

	private Utente utente;

	private Evento evento;

	private EventoCreateDTO createDto;
	
	UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

	@BeforeEach
	void setup() {
		

		organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Prova!")
				.descrizione("Squadra Calcio").tokenVerifica(123456).scadenzaToken(time).build();

		utente = Utente.builder().organizzazione(organizzazione).email("prova").passwordHash("123Prova!")
				.cognome("Rossi").nome("Marco").tokenVerifica(123456).scadenzaToken(time).build();

		evento = Evento.builder().organizzazione(organizzazione).utente(utente).tipo(TipoEvento.PERSONALE)
				.titolo("appuntamento uno").descrizione("prova prova prova").timeInizio(time).timeFine(time)
				.timeNotifica(time).build();

		createDto = EventoCreateDTO.builder().idOrganizzazione(uuid).idUtente(uuid).tipo(TipoEvento.PERSONALE)
				.titolo("appuntamento uno").descrizione("prova prova prova").timeInizio(time).timeFine(time)
				.timeNotifica(time).build();
		
		
	

	}

	@Test
	@Order(1)
	void inserimentoEventoPersonale () {
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		when(utenteRepository.findById(any(UUID.class))).thenReturn(Optional.of(utente));
		
		when(eventoMapper.toEntity(any(EventoCreateDTO.class))).thenReturn(evento);
		
		eventoServiceImpl.inserisciEvento(createDto);
		
		verify(organizzazioneRepository).findById(any(UUID.class));
		
		verify(utenteRepository).findById(any(UUID.class));
		
		verify(eventoMapper).toEntity(any(EventoCreateDTO.class));
		
		verify(eventoRepository).save(any(Evento.class));
		
	}
	
	@Test
	@Order(2)
	void modificaEventoNotFound() {
		
		when(eventoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows (EventoNotFoundException.class, ()-> eventoServiceImpl.modificaEvento(uuid, new EventoUpdateDTO()));
		
	}
	
	@Test
	@Order(3)
	void eliminaEventoNotFound () {
		when(eventoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows (EventoNotFoundException.class, ()-> eventoServiceImpl.eliminaEvento(uuid));
	}
	
	@Test
	@Order(4)
	void inserimentoEventoTeam() {
		Evento eventoTeam = Evento.builder().organizzazione(organizzazione).tipo(TipoEvento.TEAM)
				.titolo("appuntamento uno").descrizione("prova prova prova").timeInizio(time).timeFine(time)
				.timeNotifica(time).build();

		EventoCreateDTO createDtoTeam = EventoCreateDTO.builder().idOrganizzazione(uuid).tipo(TipoEvento.TEAM)
				.titolo("appuntamento uno").descrizione("prova prova prova").timeInizio(time).timeFine(time)
				.timeNotifica(time).build();
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));

		when(eventoMapper.toEntity(any(EventoCreateDTO.class))).thenReturn(eventoTeam);

		eventoServiceImpl.inserisciEvento(createDtoTeam);

		verify(organizzazioneRepository).findById(any(UUID.class));

		verify(utenteRepository, never()).findById(any(UUID.class));

		verify(eventoMapper).toEntity(any(EventoCreateDTO.class));

		verify(eventoRepository).save(any(Evento.class));
	}
	
	
}

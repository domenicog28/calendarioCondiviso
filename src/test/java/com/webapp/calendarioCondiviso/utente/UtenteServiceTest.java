package com.webapp.calendarioCondiviso.utente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.webapp.calendarioCondiviso.evento.Evento;
import com.webapp.calendarioCondiviso.evento.EventoMapper;
import com.webapp.calendarioCondiviso.evento.EventoRepository;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.exception.DuplicateEmailException;
import com.webapp.calendarioCondiviso.exception.UtenteNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;
import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteUpdateDTO;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceTest {
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	OrganizzazioneRepository organizzazioneRepository;
	
	@Mock
	EventoRepository eventoRepository;
	
	@Mock
	UtenteRepository utenteRepository;
	
	@Mock
	EventoMapper eventoMapper;

	@Mock
	UtenteMapper utenteMapper;
	
	@InjectMocks
	UtenteServiceImpl utenteServiceImpl;
	
	ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));
	
	UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
	
	private UtenteCreateDTO createDto = new UtenteCreateDTO();
	
	private Utente utente;
	
	private Organizzazione organizzazione;
	
	
	@BeforeEach
	void setup() {
		organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Prova!")
				.descrizione("Squadra Calcio").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		createDto = UtenteCreateDTO.builder().email("prova@test.it").password("123Ciao!").nome("Marco").cognome("Rossi").build();
		
		utente = Utente.builder().organizzazione(organizzazione).email("prova").passwordHash("123Prova!").cognome("Rossi").nome("Marco").tokenVerifica(123456).scadenzaToken(scadenza).build();
	}
	
	@Test
	@Order(1)
	void inserimentoUtente() {
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));

		when(utenteMapper.toEntity(any(UtenteCreateDTO.class))).thenReturn(utente);

		when(passwordEncoder.encode(anyString())).thenReturn("123Prova!");

		utenteServiceImpl.inserisciUtente(uuid, createDto);
		
		verify(organizzazioneRepository).findById(any(UUID.class));
		
		verify(passwordEncoder).encode(anyString());
		
		verify(utenteMapper).toEntity(any(UtenteCreateDTO.class));
		
		verify(utenteRepository).save(any(Utente.class));

	}
	
	@Test
	@Order(2)
	void emailUtenteDuplicata() {
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		when(passwordEncoder.encode(anyString())).thenReturn("123Prova!");
		
		when(utenteMapper.toEntity(any(UtenteCreateDTO.class))).thenReturn(utente);
		
		when(utenteRepository.save(any(Utente.class))).thenThrow(DataIntegrityViolationException.class);

		assertThrows(DuplicateEmailException.class, () -> utenteServiceImpl.inserisciUtente(uuid, createDto));
	}
	
	@Test
	@Order(3)
	void modificaUtenteNotFound() {
		UtenteUpdateDTO updateDto = new UtenteUpdateDTO();
		
		when(utenteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(UtenteNotFoundException.class, ()-> utenteServiceImpl.modificaUtente(uuid, updateDto));
	}
	
	@Test
	@Order(4)
	void ricercaPerEmailNotFound() {
		
		when(utenteRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		
		assertThrows(UtenteNotFoundException.class, ()-> utenteServiceImpl.cercaPerEmail("prova@test.it"));
	}
	
	@Test
	@Order(5)
	void ricercaPerTokenNotFound() {
		
		when(utenteRepository.findByTokenVerifica(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UtenteNotFoundException.class, ()-> utenteServiceImpl.cercaPerToken(123456));
	}
	
	@Test
	@Order(6)
	void ricercaEventiUtente() {
		List<Evento> eventiUtente = List.of(new Evento());
		
		when(utenteRepository.findById(any(UUID.class))).thenReturn(Optional.of(utente));

		when(eventoRepository.findByUtente(utente)).thenReturn(eventiUtente);

		when(eventoMapper.toListDTO(anyList())).thenReturn(List.of(new EventoResponseDTO()));

		List<EventoResponseDTO> eventiRicercati = utenteServiceImpl.cercaEventiUtente(uuid);

		assertEquals(1, eventiRicercati.size());

	}

}

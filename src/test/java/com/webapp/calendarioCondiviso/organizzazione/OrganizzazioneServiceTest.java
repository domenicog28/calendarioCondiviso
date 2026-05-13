package com.webapp.calendarioCondiviso.organizzazione;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.invito.Invito;
import com.webapp.calendarioCondiviso.invito.InvitoMapper;
import com.webapp.calendarioCondiviso.invito.InvitoRepository;
import com.webapp.calendarioCondiviso.invito.dto.InvitoResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneUpdateDTO;
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteMapper;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

@ExtendWith(MockitoExtension.class)
public class OrganizzazioneServiceTest {
	
	@Mock
	OrganizzazioneRepository organizzazioneRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	InvitoRepository invitoRepository;
	
	@Mock
	EventoRepository eventoRepository;
	
	@Mock
	UtenteRepository utenteRepository;
	
	@Mock
	InvitoMapper invitoMapper;

	@Mock
	EventoMapper eventoMapper;

	@Mock
	UtenteMapper utenteMapper;
	
	@Mock
	OrganizzazioneMapper organizzazioneMapper;
	
	@InjectMocks
	OrganizzazioneServiceImpl organizzazioneServiceImpl;
	
	@Captor
	ArgumentCaptor<Organizzazione> organizzazioneCaptor;
	
	ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));
	
	private Organizzazione organizzazione;
	
	@BeforeEach
	void setup() {
		organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Prova!")
				.descrizione("Squadra Calcio").tokenVerifica(123456).scadenzaToken(scadenza).build();
	}
	
	@Test
	@Order(1)
	void verificaInserisciOrganizzazione() {
		
		OrganizzazioneCreateDTO createDto = OrganizzazioneCreateDTO.builder().email("prova@test.it").password("123Ciao!").descrizione("Squadra Calcio").build();
		when(passwordEncoder.encode(anyString())).thenReturn("123Prova!");
		
		when(organizzazioneMapper.toEntity(any(OrganizzazioneCreateDTO.class))).thenReturn(organizzazione);
		
		organizzazioneServiceImpl.inserisciOrganizzazione(createDto);
		
		verify(organizzazioneRepository).save(organizzazioneCaptor.capture());
		
		verify(passwordEncoder).encode(anyString());
		
		Organizzazione organizzazioneSalvata = organizzazioneCaptor.getValue();
		
		assertNotEquals("123Ciao!", organizzazioneSalvata.getPasswordHash());
		
	}
	
	@Test
	@Order(2)
	void emailOrganizzazioneDuplicata() {
		OrganizzazioneCreateDTO createDto = OrganizzazioneCreateDTO.builder().email("prova@test.it")
				.password("123Ciao!").descrizione("Squadra Calcio").build();
		when(passwordEncoder.encode(anyString())).thenReturn("123Prova!");

		when(organizzazioneMapper.toEntity(any(OrganizzazioneCreateDTO.class))).thenReturn(organizzazione);
		
		when(organizzazioneRepository.save(any(Organizzazione.class))).thenThrow(DataIntegrityViolationException.class);

		assertThrows(DuplicateEmailException.class, () -> organizzazioneServiceImpl.inserisciOrganizzazione(createDto));

	}
	
	@Test
	@Order(3)
	void modificaOrganizzazioneRiuscita() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		
		OrganizzazioneUpdateDTO updateDto = OrganizzazioneUpdateDTO.builder().passwordHash("123Modifica!").descrizione("Squadra Curling").build();
		
		
		when(passwordEncoder.encode(anyString())).thenReturn("123ModificaRiuscita!");
		
		organizzazioneServiceImpl.modificaOrganizzazione(uuid, updateDto);
		
		verify(passwordEncoder).encode(anyString());
		
		verify(organizzazioneRepository).save(any(Organizzazione.class));
		
		
	}
	
	@Test
	@Order(4)
	void modificaOrganizzazioneNotFound() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
		
		OrganizzazioneUpdateDTO updateDto = OrganizzazioneUpdateDTO.builder().passwordHash("123Modifica!").descrizione("Squadra Curling").build();

		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(OrganizzazioneNotFoundException.class, ()-> organizzazioneServiceImpl.modificaOrganizzazione(uuid, updateDto));

	}
	
	@Test
	@Order(5)
	void eliminazioneOrganizzazioneRiuscita() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		organizzazioneServiceImpl.eliminaOrganizzazione(uuid);
		
		verify (organizzazioneRepository).findById(any(UUID.class));
		verify (organizzazioneRepository).delete(any(Organizzazione.class));
	}
	
	@Test
	@Order(6)
	void eliminazioneOrganizzazioneNotFound() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(OrganizzazioneNotFoundException.class, ()-> organizzazioneServiceImpl.eliminaOrganizzazione(uuid));
		
	}
	
	@Test
	@Order(7)
	void ricercaPerEmailNotFound() {
		
		when(organizzazioneRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		
		assertThrows(OrganizzazioneNotFoundException.class, ()-> organizzazioneServiceImpl.cercaPerEmail("prova@test.it"));
	}
	
	@Test
	@Order(8)
	void ricercaPerTokenNotFound() {
		
		when(organizzazioneRepository.findByTokenVerifica(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(OrganizzazioneNotFoundException.class, ()-> organizzazioneServiceImpl.cercaPerToken(123456));
	}
	
	
	
	@Test
	@Order(9)
	void cercaInvitiOrganizzazione() {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

		ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 3, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));
		ZonedDateTime time = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

		Organizzazione organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();

		Invito invito = Invito.builder().organizzazione(organizzazione).timeGenerato(time).scadenzaToken(scadenza)
				.build();

		List<Invito> lista = new ArrayList<>();

		lista.add(invito);

		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));

		when(invitoRepository.findByOrganizzazione(organizzazione)).thenReturn(lista);

		when(invitoMapper.toListDTO(anyList())).thenReturn(List.of(new InvitoResponseDTO()));

		List<InvitoResponseDTO> invitiRicercati = organizzazioneServiceImpl.cercaInvitiOrganizzazione(uuid);

		assertEquals(1, invitiRicercati.size());

	}
	
	
	@Test
	@Order(10)
	void cercaEventiOrganizzazione() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

		ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 3, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

		Organizzazione organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		List<Evento> lista = List.of(new Evento());
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		when(eventoRepository.findByOrganizzazione(organizzazione)).thenReturn(lista);
		
		when(eventoMapper.toListDTO(anyList())).thenReturn(List.of(new EventoResponseDTO()));
		
		List<EventoResponseDTO> eventiRicercati = organizzazioneServiceImpl.cercaEventiOrganizzazione(uuid);
		
		assertEquals(1, eventiRicercati.size());
	}
	
	
	@Test
	@Order(11)
	void cercaUtentiOrganizzazione() {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

		ZonedDateTime scadenza = ZonedDateTime.of(2026, 5, 3, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));
		
		Organizzazione organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Ciao!")
				.descrizione("SquadraCalcio").tokenVerifica(123456).scadenzaToken(scadenza).build();
		
		List<Utente> lista = List.of(new Utente());
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		when(utenteRepository.findByOrganizzazione(organizzazione)).thenReturn(lista);
		
		when(utenteMapper.toListDTO(anyList())).thenReturn(List.of(new UtenteResponseDTO()));
		
		List<UtenteResponseDTO> utentiRicercati = organizzazioneServiceImpl.cercaUtentiOrganizzazione(uuid);
		
		assertEquals(1, utentiRicercati.size());
		
	}
	
	
	

}

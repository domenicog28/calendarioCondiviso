package com.webapp.calendarioCondiviso.invito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.webapp.calendarioCondiviso.exception.ExpiredTokenException;
import com.webapp.calendarioCondiviso.exception.InvitoNotFoundException;
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.invito.dto.InvitoCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;

@ExtendWith(MockitoExtension.class)
public class InvitoServiceTest {
	
	@Mock
	OrganizzazioneRepository organizzazioneRepository;
	
	@Mock
	InvitoMapper invitoMapper;
	
	@Mock
	InvitoRepository invitoRepository;
	
	@InjectMocks
	InvitoServiceImpl invitoServiceImpl;
	
	ZonedDateTime time = ZonedDateTime.of(2026, 5, 2, 14, 30, 0, 0, ZoneId.of("Europe/Rome"));

	UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
	
	private Organizzazione organizzazione;
	
	private InvitoCreateDTO createDto;
	
	private Invito invito;
	
	@BeforeEach
	void setup() {
		organizzazione = Organizzazione.builder().email("prova@test.it").passwordHash("123Prova!")
				.descrizione("Squadra Calcio").tokenVerifica(123456).scadenzaToken(time).build();
		
		createDto = InvitoCreateDTO.builder().idOrganizzazione(uuid).build();
		
		invito = Invito.builder().organizzazione(organizzazione).timeGenerato(time).scadenzaToken(time).build();
		
	}
	
	@Test
	@Order(1)
	void inserimentoToken() {
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.of(organizzazione));
		
		when(invitoMapper.toEntity(any(InvitoCreateDTO.class))).thenReturn(invito);
		
		invitoServiceImpl.inserisciInvito(createDto);
		
		verify(organizzazioneRepository).findById(any(UUID.class));
		
		verify(invitoMapper).toEntity(any(InvitoCreateDTO.class));
		
		verify(invitoRepository).save(any(Invito.class));

	}
	
	@Test
	@Order(2)
	void inserimentoOrganizzazioneNotFound() {
		
		when(organizzazioneRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(OrganizzazioneNotFoundException.class, ()-> invitoServiceImpl.inserisciInvito(createDto));
		
	}
	
	@Test
	@Order(3)
	void verificaTokenNotFound () {
		
		when(invitoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(InvitoNotFoundException.class, ()-> invitoServiceImpl.verificaToken(uuid));
	}
	
	@Test
	@Order(4)
	void verificaExpiredToken () {
		
		when(invitoRepository.findById(any(UUID.class))).thenReturn(Optional.of(invito));
		
		assertThrows(ExpiredTokenException.class, ()-> invitoServiceImpl.verificaToken(uuid));
		
	}
		
}

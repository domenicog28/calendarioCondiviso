package com.webapp.calendarioCondiviso.evento;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoUpdateDTO;
import com.webapp.calendarioCondiviso.exception.EventoNotFoundException;
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.exception.UtenteNotFoundException;


@WebMvcTest(value = EventoController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class EventoControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	EventoService eventoService;
	
	UUID uuidOrganizzazione = UUID.fromString("00000000-0000-0000-0000-000000000000");

	UUID uuidUtente = UUID.fromString("00000000-0000-0000-0000-000000000001");
	
	ZonedDateTime time = ZonedDateTime.now();

	@Test
	@Order(1)
	void registrazioneEventoOrganizzazioneOk() throws Exception {
		
		EventoCreateDTO createDto = EventoCreateDTO.builder()
				.idOrganizzazione(uuidOrganizzazione)
				.tipo(TipoEvento.TEAM)
				.titolo("Evento Prova")
				.descrizione("Primo Evento")
				.timeInizio(time)
				.timeFine(time)
				.timeNotifica(time)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule());
		
		String json = objectMapper.writeValueAsString(createDto);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/eventi/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();
		
		verify(eventoService).inserisciEvento(any(EventoCreateDTO.class));
		
	}
	
	@Test
	@Order(2)
	void registrazioneEventoOrganizzazioneErrore() throws Exception {
		
		EventoCreateDTO createDto = EventoCreateDTO.builder()
				.idOrganizzazione(uuidOrganizzazione)
				.tipo(TipoEvento.TEAM)
				.titolo("Evento Prova")
				.descrizione("Primo Evento")
				.timeInizio(time)
				.timeFine(time)
				.timeNotifica(time)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule());
		
		String json = objectMapper.writeValueAsString(createDto);
		
		doThrow(OrganizzazioneNotFoundException.class).when(eventoService).inserisciEvento(any(EventoCreateDTO.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/eventi/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		
		verify(eventoService).inserisciEvento(any(EventoCreateDTO.class));
		
	}
	
	
	@Test
	@Order(3)
	void registrazioneEventoUtenteOk() throws Exception {
		
		EventoCreateDTO createDto = EventoCreateDTO.builder()
				.idOrganizzazione(uuidOrganizzazione)
				.idUtente(uuidUtente)
				.tipo(TipoEvento.PERSONALE)
				.titolo("Evento Prova")
				.descrizione("Primo Evento")
				.timeInizio(time)
				.timeFine(time)
				.timeNotifica(time)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule());
		
		String json = objectMapper.writeValueAsString(createDto);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/eventi/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();
		
		verify(eventoService).inserisciEvento(any(EventoCreateDTO.class));
	}
	
	@Test
	@Order(4)
	void registrazioneEventoUtenteErrore() throws Exception {
		
		EventoCreateDTO createDto = EventoCreateDTO.builder()
				.idOrganizzazione(uuidOrganizzazione)
				.idUtente(uuidUtente)
				.tipo(TipoEvento.PERSONALE)
				.titolo("Evento Prova")
				.descrizione("Primo Evento")
				.timeInizio(time)
				.timeFine(time)
				.timeNotifica(time)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule());
		
		String json = objectMapper.writeValueAsString(createDto);
		
		doThrow(UtenteNotFoundException.class).when(eventoService).inserisciEvento(any(EventoCreateDTO.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/eventi/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		
		verify(eventoService).inserisciEvento(any(EventoCreateDTO.class));
		
	}
	
	@Test
	@Order(5)
	void modificaEventoOk() throws Exception {
		
		UUID uuid= UUID.fromString("00000000-0000-0000-0000-000000000002");
		
		EventoUpdateDTO updateDto = EventoUpdateDTO.builder()
				.titolo("modifica titolo")
				.descrizione("modifica descrizione")
				.timeInizio(time)
				.timeFine(time)
				.timeNotifica(time)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule());

		String json = objectMapper.writeValueAsString(updateDto);
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/eventi/modifica/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		verify(eventoService).modificaEvento(any(UUID.class), any(EventoUpdateDTO.class));
		
	}
	
	@Test
	@Order(6)
	void modificaEventoErrore() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");

		EventoUpdateDTO updateDto = EventoUpdateDTO.builder()
				.titolo("modifica titolo")
				.descrizione("modifica descrizione")
				.timeInizio(time)
				.timeFine(time)
				.timeNotifica(time)
				.build();

		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule());

		String json = objectMapper.writeValueAsString(updateDto);
		
		doThrow(EventoNotFoundException.class).when(eventoService).modificaEvento(any(UUID.class), any(EventoUpdateDTO.class));

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/eventi/modifica/{uuid}", uuid)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

		verify(eventoService).modificaEvento(any(UUID.class), any(EventoUpdateDTO.class));
		
	}
	
	@Test
	@Order(7)
	void eliminaEventoOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
		
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/eventi/elimina/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andReturn();
		
		verify(eventoService).eliminaEvento(any(UUID.class));
		
		
	}
	
	@Test
	@Order(8)
	void eliminaEventoErrore() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
		
		doThrow(EventoNotFoundException.class).when(eventoService).eliminaEvento(any(UUID.class));

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/eventi/elimina/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

		verify(eventoService).eliminaEvento(any(UUID.class));
		
	}
	
	@Test
	@Order(9)
	void eventoResponseUUIDOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
		
		when(eventoService.cercaPerId(any(UUID.class))).thenReturn(new EventoResponseDTO());
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/eventi/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		verify(eventoService).cercaPerId(any(UUID.class));
		
	}
	
	@Test
	@Order(10)
	void eventoResponseUUIDErrore() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
		
		doThrow(EventoNotFoundException.class).when(eventoService).cercaPerId(any(UUID.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/eventi/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		
		verify(eventoService).cercaPerId(any(UUID.class));
		
	}

}

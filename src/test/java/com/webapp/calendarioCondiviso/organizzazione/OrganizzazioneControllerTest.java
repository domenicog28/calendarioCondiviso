package com.webapp.calendarioCondiviso.organizzazione;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
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
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.exception.DuplicateEmailException;
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneUpdateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

@WebMvcTest(value = OrganizzazioneController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class OrganizzazioneControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	OrganizzazioneService organizzazioneService;
	
	
	@Test
	@Order(1)
	void inserimentoOrganizzazioneOk() throws Exception {
		
		OrganizzazioneCreateDTO createDto = OrganizzazioneCreateDTO.builder()
				.email("test@gmail.com")
				.password("prova123")
				.descrizione("descrizione di prova").build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(createDto);
		
		
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/organizzazioni/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();
		
		verify(organizzazioneService).inserisciOrganizzazione(any(OrganizzazioneCreateDTO.class));
		
	}
	
	@Test
	@Order(2)
	void inserimentoOrganizzazioneErrore() throws Exception {
		
		OrganizzazioneCreateDTO createDto = OrganizzazioneCreateDTO.builder()
				.email("test@gmail.com")
				.password("prova123")
				.descrizione("descrizione di prova").build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(createDto);
		
		doThrow(DuplicateEmailException.class).when(organizzazioneService).inserisciOrganizzazione(any(OrganizzazioneCreateDTO.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/organizzazioni/registrazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				.andReturn();
		
	}
	
	@Test
	@Order(3)
	void modificaOrganizzazioneOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		OrganizzazioneUpdateDTO updateDto = OrganizzazioneUpdateDTO.builder()
				.password("prova1234")
				.descrizione("descrizione di prova 2").build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(updateDto);
		
		
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/organizzazioni/modifica/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		verify(organizzazioneService).modificaOrganizzazione(any(UUID.class), any(OrganizzazioneUpdateDTO.class));
		
	}
	
	@Test
	@Order(4)
	void modificaOrganizzazioneNotFound() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		OrganizzazioneCreateDTO updateDto = OrganizzazioneCreateDTO.builder()
				.password("prova1234")
				.descrizione("descrizione di prova 2").build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(updateDto);
		
		doThrow(OrganizzazioneNotFoundException.class).when(organizzazioneService).modificaOrganizzazione(any(UUID.class), any(OrganizzazioneUpdateDTO.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/organizzazioni/modifica/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		
		verify(organizzazioneService).modificaOrganizzazione(any(UUID.class), any(OrganizzazioneUpdateDTO.class));
		
	}
	
	@Test
	@Order(5)
	void eliminaOrganizzazioneOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		
		
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/organizzazioni/elimina/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andReturn();
				
		verify(organizzazioneService).eliminaOrganizzazione(any(UUID.class));
		
	}
	
	@Test
	@Order(6)
	void eliminaOrganizzazioneNotFound() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		doThrow(OrganizzazioneNotFoundException.class).when(organizzazioneService).eliminaOrganizzazione(any(UUID.class));

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/organizzazioni/elimina/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		

		verify(organizzazioneService).eliminaOrganizzazione(any(UUID.class));

	}
	
	@Test
	@Order(7)
	void eventiOrganizzazione() throws Exception{
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		when(organizzazioneService.cercaEventiOrganizzazione(any(UUID.class))).thenReturn(List.of(new EventoResponseDTO()));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/organizzazioni/{uuid}/eventi", uuid))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andReturn();
		
		verify(organizzazioneService).cercaEventiOrganizzazione(any(UUID.class));
	}
	
	@Test
	@Order(8)
	void utentiOrganizzazione() throws Exception{
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		when(organizzazioneService.cercaUtentiOrganizzazione(any(UUID.class))).thenReturn(List.of(new UtenteResponseDTO()));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/organizzazioni/{uuid}/utenti", uuid))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andReturn();
		
		verify(organizzazioneService).cercaUtentiOrganizzazione(any(UUID.class));
	}
	
	@Test
	@Order(9)
	void organizzazioneResponseUUIDOk() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

		when(organizzazioneService.cercaPerUUID(any(UUID.class))).thenReturn(new OrganizzazioneResponseDTO());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/organizzazioni/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(organizzazioneService).cercaPerUUID(any(UUID.class));

	}
	
	@Test
	@Order(10)
	void organizzazioneResponseUUIDError() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

		doThrow(OrganizzazioneNotFoundException.class).when(organizzazioneService).cercaPerUUID(any(UUID.class));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/organizzazioni/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();

		verify(organizzazioneService).cercaPerUUID(any(UUID.class));

	}
	
	
	
	

}

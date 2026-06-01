package com.webapp.calendarioCondiviso.utente;

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
import com.webapp.calendarioCondiviso.exception.UtenteNotFoundException;
import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteUpdateDTO;

@WebMvcTest(value = UtenteController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UtenteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	UtenteService utenteService;
	
	@Test
	@Order(1)
	void registrazioneUtenteOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		UtenteCreateDTO createDto = UtenteCreateDTO.builder()
				.email("prova@test.it")
				.password("123Prova!")
				.nome("Marco")
				.cognome("Rossi").build();
		
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(createDto);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/utenti/registrazione/{uuidOrganizzazione}", uuid.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andReturn();
		
		verify(utenteService).inserisciUtente(any(UUID.class), any(UtenteCreateDTO.class));
		
	}
	
	@Test
	@Order(2)
	void registrazioneUtenteErrore() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		UtenteCreateDTO createDto = UtenteCreateDTO.builder()
				.email("prova@test.it")
				.password("123Prova!")
				.nome("Marco")
				.cognome("Rossi").build();
		
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(createDto);
		
		doThrow(DuplicateEmailException.class).when(utenteService).inserisciUtente(any(UUID.class), any(UtenteCreateDTO.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/utenti/registrazione/{uuidOrganizzazione}", uuid.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isConflict())
				.andReturn();
		
		verify(utenteService).inserisciUtente(any(UUID.class), any(UtenteCreateDTO.class));
		
	}
	
	@Test
	@Order(3)
	void modificaUtenteOk () throws Exception {
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		UtenteUpdateDTO updateDto = UtenteUpdateDTO.builder()
				.password("prova1234")
				.nome("Filippo")
				.cognome("Primo")
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(updateDto);
		
		
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/utenti/modifica/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		verify(utenteService).modificaUtente(any(UUID.class), any(UtenteUpdateDTO.class));
	}
	
	@Test
	@Order(4)
	void modificaUtenteErrore () throws Exception{
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		UtenteUpdateDTO updateDto = UtenteUpdateDTO.builder()
				.password("prova1234")
				.nome("Filippo")
				.cognome("Primo")
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(updateDto);
		
		doThrow(UtenteNotFoundException.class).when(utenteService).modificaUtente(any(UUID.class), any(UtenteUpdateDTO.class));;
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/utenti/modifica/{uuid}", uuid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		
		verify(utenteService).modificaUtente(any(UUID.class), any(UtenteUpdateDTO.class));
		
	}
	
	@Test
	@Order(5)
	void eliminaUtenteOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/utenti/elimina/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNoContent())
				.andReturn();
				
		verify(utenteService).eliminaUtente(any(UUID.class));
		
	}
	
	@Test
	@Order(6)
	void eliminaUtenteNotFound() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		doThrow(UtenteNotFoundException.class).when(utenteService).eliminaUtente(any(UUID.class));;

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/utenti/elimina/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();
		

		verify(utenteService).eliminaUtente(any(UUID.class));

	}
	
	@Test
	@Order(7)
	void eventiUtente() throws Exception{
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		when(utenteService.cercaEventiUtente(any(UUID.class))).thenReturn(List.of(new EventoResponseDTO()));
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/utenti/{uuid}/eventi", uuid))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andReturn();
		
		verify(utenteService).cercaEventiUtente(any(UUID.class));
	}
	
	@Test
	@Order(8)
	void utenteResponseUUIDOk() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

		when(utenteService.cercaPerUUID(any(UUID.class))).thenReturn(new UtenteResponseDTO());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/utenti/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		verify(utenteService).cercaPerUUID(any(UUID.class));

	}
	
	@Test
	@Order(9)
	void utenteResponseUUIDError() throws Exception {

		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

		doThrow(UtenteNotFoundException.class).when(utenteService).cercaPerUUID(any(UUID.class));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/utenti/{uuid}", uuid))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andReturn();

		verify(utenteService).cercaPerUUID(any(UUID.class));

	}
	
	

}

package com.webapp.calendarioCondiviso.invito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

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
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.invito.dto.InvitoCreateDTO;

@WebMvcTest(value = InvitoController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class InvitoControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	InvitoService invitoService;
	
	@Test
	@Order(1)
	void registrazioneInvitoOk() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		InvitoCreateDTO createDto = InvitoCreateDTO.builder()
				.idOrganizzazione(uuid)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(createDto);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/inviti/registrazione")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andReturn();
		
		verify(invitoService).inserisciInvito(any(InvitoCreateDTO.class));
		
	}
	
	@Test
	@Order(2)
	void registrazioneInvitoErrore() throws Exception {
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		
		InvitoCreateDTO createDto = InvitoCreateDTO.builder()
				.idOrganizzazione(uuid)
				.build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String json = objectMapper.writeValueAsString(createDto);
		
		doThrow(OrganizzazioneNotFoundException.class).when(invitoService).inserisciInvito(any(InvitoCreateDTO.class));
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/inviti/registrazione")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andReturn();
		
		verify(invitoService).inserisciInvito(any(InvitoCreateDTO.class));
		
	}

}

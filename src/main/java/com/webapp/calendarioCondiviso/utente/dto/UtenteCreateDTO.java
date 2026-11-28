package com.webapp.calendarioCondiviso.utente.dto;

//import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteCreateDTO {
	
	//private UUID tokenInvito;

	private String email;

	private String password;

	private String nome;

	private String cognome;

}

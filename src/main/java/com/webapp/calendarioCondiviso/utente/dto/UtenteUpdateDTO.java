package com.webapp.calendarioCondiviso.utente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteUpdateDTO {
	
	private String password;
	
	private String nome;
	
	private String cognome;

}

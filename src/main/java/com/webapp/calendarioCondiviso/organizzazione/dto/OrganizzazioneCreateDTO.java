package com.webapp.calendarioCondiviso.organizzazione.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizzazioneCreateDTO {

	private String email;

	private String password;

	private String descrizione;

}

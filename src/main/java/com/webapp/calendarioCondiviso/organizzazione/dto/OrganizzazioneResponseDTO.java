package com.webapp.calendarioCondiviso.organizzazione.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizzazioneResponseDTO {

	private UUID idOrganizzazione;

	private String email;

	private String descrizione;
}

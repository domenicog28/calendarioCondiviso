package com.webapp.calendarioCondiviso.utente.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteResponseDTO {

	private UUID idUtente;

	private UUID idOrganizzazione;

	private String email;

}

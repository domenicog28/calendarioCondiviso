package com.webapp.calendarioCondiviso.invito.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitoResponseDTO {

	private UUID idToken;

	private UUID idOrganizzazione;

	private ZonedDateTime scadenzaToken;
}

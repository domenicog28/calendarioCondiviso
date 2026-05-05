package com.webapp.calendarioCondiviso.invito.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitoCreateDTO {

	private UUID idOrganizzazione;

}

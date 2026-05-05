package com.webapp.calendarioCondiviso.evento.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.webapp.calendarioCondiviso.evento.TipoEvento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoResponseDTO {

	private UUID idEvento;
	
	private TipoEvento tipo;

	private String titolo;

	private String descrizione;

	private ZonedDateTime timeInizio;

	private ZonedDateTime timeFine;
}

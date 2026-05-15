package com.webapp.calendarioCondiviso.evento.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoUpdateDTO {
	
	private String titolo;
	
	private String descrizione;
	
	private ZonedDateTime timeInizio;
	
	private ZonedDateTime timeFine;
	
	private ZonedDateTime timeNotifica;
	
}

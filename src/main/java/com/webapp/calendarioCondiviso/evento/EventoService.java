package com.webapp.calendarioCondiviso.evento;

import java.util.UUID;

import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoUpdateDTO;

public interface EventoService {
	
	public void inserisciEvento (EventoCreateDTO createDto);
	
	public void modificaEvento (UUID uuid, EventoUpdateDTO updateDto);
	
	public void eliminaEvento (UUID uuid);
	
	public EventoResponseDTO cercaPerId(UUID uuid);

}

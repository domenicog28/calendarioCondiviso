package com.webapp.calendarioCondiviso.evento;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;

@Mapper
public interface EventoMapper {
	
	@Mapping(target= "idEvento", ignore=true)
	Evento toEntity(EventoCreateDTO createDTO);
	
	EventoResponseDTO toDto(Evento evento);

}

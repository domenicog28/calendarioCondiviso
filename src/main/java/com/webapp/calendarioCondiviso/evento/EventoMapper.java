package com.webapp.calendarioCondiviso.evento;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;

@Mapper(componentModel = "spring")
public interface EventoMapper {
	
	@Mapping(target= "idEvento", ignore=true)
	Evento toEntity(EventoCreateDTO createDTO);
	
	EventoResponseDTO toDto(Evento evento);

	List<EventoResponseDTO> toListDTO(List<Evento> eventi);

}

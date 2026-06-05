package com.webapp.calendarioCondiviso.organizzazione;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneResponseDTO;

@Mapper(componentModel = "spring")
public interface OrganizzazioneMapper {
	
	@Mapping(target = "idOrganizzazione", ignore = true)
	Organizzazione toEntity(OrganizzazioneCreateDTO createDTO);
	
	OrganizzazioneResponseDTO toDto(Organizzazione organizzazione);
}

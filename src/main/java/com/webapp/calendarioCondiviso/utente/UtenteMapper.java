package com.webapp.calendarioCondiviso.utente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

@Mapper
public interface UtenteMapper {
	
	@Mapping(target = "idUtente", ignore = true)
	Utente toEntity(UtenteCreateDTO createDTO);
	
	UtenteResponseDTO toDto(Utente utente);

}

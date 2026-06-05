package com.webapp.calendarioCondiviso.utente;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

@Mapper(componentModel = "spring")
public interface UtenteMapper {
	
	@Mapping(target = "idUtente", ignore = true)
	Utente toEntity(UtenteCreateDTO createDTO);
	
	UtenteResponseDTO toDto(Utente utente);
	
	List <UtenteResponseDTO> toListDTO (List<Utente> utenti);

}

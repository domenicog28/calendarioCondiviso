package com.webapp.calendarioCondiviso.invito;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.webapp.calendarioCondiviso.invito.dto.InvitoCreateDTO;
import com.webapp.calendarioCondiviso.invito.dto.InvitoResponseDTO;

@Mapper
public interface InvitoMapper {

	@Mapping(target = "idToken", ignore = true)
	Invito toEntity(InvitoCreateDTO createDTO);

	InvitoResponseDTO toDto(Invito invito);
}

package com.webapp.calendarioCondiviso.invito;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.calendarioCondiviso.TokenUtils;
import com.webapp.calendarioCondiviso.exception.ExpiredTokenException;
import com.webapp.calendarioCondiviso.exception.InvitoNotFoundException;
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.invito.dto.InvitoCreateDTO;
import com.webapp.calendarioCondiviso.invito.dto.InvitoResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;

@Service
public class InvitoServiceImpl implements InvitoService{
	
	@Autowired
	OrganizzazioneRepository organizzazioneRepository;
	
	@Autowired
	InvitoMapper invitoMapper;
	
	@Autowired
	InvitoRepository invitoRepository;

	@Override
	public void inserisciInvito(InvitoCreateDTO createDto) {

		Organizzazione organizzazione = organizzazioneRepository.findById(createDto.getIdOrganizzazione())
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		Invito invito = invitoMapper.toEntity(createDto);
		
		invito.setOrganizzazione(organizzazione);
		
		invito.setTimeGenerato(ZonedDateTime.now(ZoneId.of("Europe/Rome")));
		
		invito.setScadenzaToken(TokenUtils.generaScadenzaTokenInvito());
		
		invitoRepository.save(invito);
		
	}

	@Override
	public InvitoResponseDTO verificaToken(UUID uuid) {

		Invito invito = invitoRepository.findById(uuid)
				.orElseThrow(()-> new InvitoNotFoundException("Invito non trovato!"));
		
		if (invito.getScadenzaToken().isBefore(ZonedDateTime.now(ZoneId.of("Europe/Rome")))) {
			
			throw new ExpiredTokenException ("Invito"+uuid.toString()+" scaduto!");
		}
		
		return invitoMapper.toDto(invito);
	}

	@Override
	public void eliminaInvito(UUID uuid) {

		Invito invito = invitoRepository.findById(uuid)
				.orElseThrow(()-> new InvitoNotFoundException ("Invito non trovato!"));
		
		invitoRepository.delete(invito);
		
	}

}

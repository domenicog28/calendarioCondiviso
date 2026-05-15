package com.webapp.calendarioCondiviso.evento;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.calendarioCondiviso.evento.dto.EventoCreateDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.evento.dto.EventoUpdateDTO;
import com.webapp.calendarioCondiviso.exception.EventoNotFoundException;
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.exception.UtenteNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;


@Service
public class EventoServiceImpl implements EventoService {
	
	@Autowired
	EventoMapper eventoMapper;
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Autowired
	OrganizzazioneRepository organizzazioneRepository;
	
	@Autowired
	EventoRepository eventoRepository;

	@Override
	public void inserisciEvento(EventoCreateDTO createDto) {
		if (createDto.getTipo().equals(TipoEvento.TEAM)) {
			
			Organizzazione organizzazione = organizzazioneRepository.findById(createDto.getIdOrganizzazione())
					.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
			
			Evento evento = eventoMapper.toEntity(createDto);
			
			evento.setOrganizzazione(organizzazione);
			
			eventoRepository.save(evento);
		}
		
		if (createDto.getTipo().equals(TipoEvento.PERSONALE)) {
			
			Utente utente = utenteRepository.findById(createDto.getIdUtente())
					.orElseThrow(()-> new UtenteNotFoundException ("Utente non trovato!"));
			
			Organizzazione organizzazione = organizzazioneRepository.findById(createDto.getIdOrganizzazione())
					.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
			
			Evento evento = eventoMapper.toEntity(createDto);
			
			evento.setOrganizzazione(organizzazione);
			
			evento.setUtente(utente);
			
			eventoRepository.save(evento);
		}
		
	}

	@Override
	public void modificaEvento(UUID uuid, EventoUpdateDTO updateDto) {

		Evento evento = eventoRepository.findById(uuid)
				.orElseThrow(() -> new EventoNotFoundException("Evento non trovato!"));

		if (updateDto.getTitolo() != null) {

			evento.setTitolo(updateDto.getTitolo());

		}

		if (updateDto.getDescrizione() != null) {

			evento.setDescrizione(updateDto.getDescrizione());

		}

		if (updateDto.getTimeInizio() != null) {

			evento.setTimeInizio(updateDto.getTimeInizio());

		}

		if (updateDto.getTimeFine() != null) {

			evento.setTimeFine(updateDto.getTimeFine());

		}

		if (updateDto.getTimeNotifica() != null) {

			evento.setTimeNotifica(updateDto.getTimeNotifica());

		}
		
		eventoRepository.save(evento);

	}

	@Override
	public void eliminaEvento(UUID uuid) {
		
		Evento evento = eventoRepository.findById(uuid)
				.orElseThrow(()-> new EventoNotFoundException("Evento non trovato!"));
		
		eventoRepository.delete(evento);
		
	}

	@Override
	public EventoResponseDTO cercaPerId(UUID uuid) {

		Evento evento = eventoRepository.findById(uuid)
				.orElseThrow(()-> new EventoNotFoundException("Evento non trovato!"));
		
		return eventoMapper.toDto(evento);
		
	}

}

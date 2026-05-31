package com.webapp.calendarioCondiviso.organizzazione;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.calendarioCondiviso.TokenUtils;
import com.webapp.calendarioCondiviso.evento.Evento;
import com.webapp.calendarioCondiviso.evento.EventoMapper;
import com.webapp.calendarioCondiviso.evento.EventoRepository;
import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.exception.DuplicateEmailException;
import com.webapp.calendarioCondiviso.exception.OrganizzazioneNotFoundException;
import com.webapp.calendarioCondiviso.invito.Invito;
import com.webapp.calendarioCondiviso.invito.InvitoMapper;
import com.webapp.calendarioCondiviso.invito.InvitoRepository;
import com.webapp.calendarioCondiviso.invito.dto.InvitoResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneUpdateDTO;
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteMapper;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

@Service
public class OrganizzazioneServiceImpl implements OrganizzazioneService {
	
	@Autowired
	OrganizzazioneMapper organizzazioneMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	InvitoRepository invitoRepository; 
	
	@Autowired
	EventoRepository eventoRepository; 
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Autowired
	InvitoMapper invitoMapper;
	
	@Autowired
	EventoMapper eventoMapper;
	
	@Autowired
	UtenteMapper utenteMapper;
	
	
	@Autowired
	OrganizzazioneRepository organizzazioneRepository;

	@Override
	public void inserisciOrganizzazione(OrganizzazioneCreateDTO createDto) {
		Organizzazione organizzazione = organizzazioneMapper.toEntity(createDto);
		
		organizzazione.setPasswordHash(passwordEncoder.encode(createDto.getPassword()));
		
		organizzazione.setTokenVerifica(TokenUtils.generaToken());
		
		organizzazione.setScadenzaToken(TokenUtils.generaScadenzaToken());
		
		try {
		
			organizzazioneRepository.save(organizzazione);
			
		} catch (DataIntegrityViolationException msg) {
			
			throw new DuplicateEmailException("Email: "+organizzazione.getEmail()+" è già utilizzata!");
		}
		
	}

	@Override
	public void modificaOrganizzazione(UUID uuid, OrganizzazioneUpdateDTO updateDto) {
		Organizzazione organizzazione = organizzazioneRepository.findById(uuid)
				.orElseThrow(() -> new OrganizzazioneNotFoundException("Organizzazione non trovata"));
		
		if (updateDto.getPasswordHash() != null) {
			
			organizzazione.setPasswordHash(passwordEncoder.encode(updateDto.getPasswordHash()));
			
		} 
		
		if (updateDto.getDescrizione() != null) {
			
			organizzazione.setDescrizione(updateDto.getDescrizione());
			
		}
		
		organizzazioneRepository.save(organizzazione);
		

	}

	@Override
	public void eliminaOrganizzazione(UUID uuid) {

		Organizzazione organizzazione = organizzazioneRepository.findById(uuid)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		organizzazioneRepository.delete(organizzazione);

	}

	@Override
	public OrganizzazioneResponseDTO cercaPerEmail(String email) {
		Organizzazione organizzazione = organizzazioneRepository.findByEmail(email)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		return organizzazioneMapper.toDto(organizzazione);
		
	}

	@Override
	public OrganizzazioneResponseDTO cercaPerToken(int token) {
		Organizzazione organizzazione = organizzazioneRepository.findByTokenVerifica(token)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		return organizzazioneMapper.toDto(organizzazione);
		
	}

	@Override
	public OrganizzazioneResponseDTO cercaPerUUID(UUID uuid) {
		Organizzazione organizzazione = organizzazioneRepository.findById(uuid)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		return organizzazioneMapper.toDto(organizzazione);
		
	}

	@Override
	public List<InvitoResponseDTO> cercaInvitiOrganizzazione(UUID uuid) {
		Organizzazione organizzazione = organizzazioneRepository.findById(uuid)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		List<Invito> inviti = invitoRepository.findByOrganizzazione(organizzazione);
		
		return invitoMapper.toListDTO(inviti);
	}

	@Override
	public List<EventoResponseDTO> cercaEventiOrganizzazione(UUID uuid) {
		Organizzazione organizzazione = organizzazioneRepository.findById(uuid)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		List<Evento> eventi = eventoRepository.findByOrganizzazione(organizzazione);
		
		return eventoMapper.toListDTO(eventi);
	}

	@Override
	public List<UtenteResponseDTO> cercaUtentiOrganizzazione(UUID uuid) {
		Organizzazione organizzazione = organizzazioneRepository.findById(uuid)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		List<Utente> utenti = utenteRepository.findByOrganizzazione(organizzazione);
		
		return utenteMapper.toListDTO(utenti);
	}
	

}

package com.webapp.calendarioCondiviso.utente;

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
import com.webapp.calendarioCondiviso.exception.UtenteNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;
import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteUpdateDTO;

@Service
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired
	OrganizzazioneRepository organizzazioneRepository;
	
	@Autowired
	UtenteMapper utenteMapper;
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	EventoRepository eventoRepository;
	
	@Autowired
	EventoMapper eventoMapper;

	@Override
	public void inserisciUtente(UUID uuidOrganizzazione, UtenteCreateDTO createDto) {
		Organizzazione organizzazione = organizzazioneRepository.findById(uuidOrganizzazione)
				.orElseThrow(()-> new OrganizzazioneNotFoundException("Organizzazione non trovata!"));
		
		Utente utente = utenteMapper.toEntity(createDto);
		
		utente.setOrganizzazione(organizzazione);
		
		utente.setPasswordHash(passwordEncoder.encode(createDto.getPassword()));
		
		utente.setTokenVerifica(TokenUtils.generaToken());
		
		utente.setScadenzaToken(TokenUtils.generaScadenzaToken());
		
		try {
			
			utenteRepository.save(utente);
			
		} catch (DataIntegrityViolationException msg) {
			
			throw new DuplicateEmailException("Email: "+utente.getEmail()+" è già utilizzata!");
		}
		

	}

	@Override
	public void modificaUtente(UUID uuid, UtenteUpdateDTO updateDto) {
		Utente utente = utenteRepository.findById(uuid)
				.orElseThrow(() -> new UtenteNotFoundException("Utente non trovato!"));

		if (updateDto.getPassword() != null) {

			utente.setPasswordHash(passwordEncoder.encode(updateDto.getPassword()));

		}

		if (updateDto.getNome() != null) {

			utente.setNome(updateDto.getNome());

		}
		
		if (updateDto.getCognome() != null) {
			
			utente.setCognome(updateDto.getCognome());
			
		}

		utenteRepository.save(utente);

	}

	@Override
	public void eliminaUtente(UUID uuid) {
		Utente utente = utenteRepository.findById(uuid)
				.orElseThrow(()-> new UtenteNotFoundException("Utente non trovato!"));
		
		utenteRepository.delete(utente);

	}

	@Override
	public UtenteResponseDTO cercaPerEmail(String email) {
		Utente utente = utenteRepository.findByEmail(email)
				.orElseThrow(()-> new UtenteNotFoundException("Utente non trovato!"));
		
		return utenteMapper.toDto(utente);
	}

	@Override
	public UtenteResponseDTO cercaPerToken(int token) {
		Utente utente = utenteRepository.findByTokenVerifica(token)
				.orElseThrow(()-> new UtenteNotFoundException("Utente non trovato!"));
		
		return utenteMapper.toDto(utente);
	}

	@Override
	public UtenteResponseDTO cercaPerUUID(UUID uuid) {
		Utente utente = utenteRepository.findById(uuid)
				.orElseThrow(()-> new UtenteNotFoundException("Utente non trovato!"));
		
		return utenteMapper.toDto(utente);
	}

	@Override
	public List<EventoResponseDTO> cercaEventiUtente(UUID uuid) {
		
		Utente utente = utenteRepository.findById(uuid)
				.orElseThrow(()-> new UtenteNotFoundException("Utente non trovato!"));
		
		List<Evento> eventiCercato = eventoRepository.findByUtente(utente);
		
		return eventoMapper.toListDTO(eventiCercato);
	}

}

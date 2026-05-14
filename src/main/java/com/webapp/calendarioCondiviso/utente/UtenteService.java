package com.webapp.calendarioCondiviso.utente;

import java.util.List;
import java.util.UUID;

import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteCreateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteUpdateDTO;

public interface UtenteService {

	public void inserisciUtente(UUID uuidOrganizzazione, UtenteCreateDTO createDto);

	public void modificaUtente(UUID uuid, UtenteUpdateDTO updateDto);

	public void eliminaUtente(UUID uuid);

	public UtenteResponseDTO cercaPerEmail(String email);

	public UtenteResponseDTO cercaPerToken(int token);

	public UtenteResponseDTO cercaPerUUID(UUID uuid);

	public List<EventoResponseDTO> cercaEventiUtente(UUID uuid);

}

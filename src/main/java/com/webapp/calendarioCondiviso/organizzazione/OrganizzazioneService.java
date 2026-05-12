package com.webapp.calendarioCondiviso.organizzazione;

import java.util.List;
import java.util.UUID;

import com.webapp.calendarioCondiviso.evento.dto.EventoResponseDTO;
import com.webapp.calendarioCondiviso.invito.dto.InvitoResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneCreateDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneResponseDTO;
import com.webapp.calendarioCondiviso.organizzazione.dto.OrganizzazioneUpdateDTO;
import com.webapp.calendarioCondiviso.utente.dto.UtenteResponseDTO;

public interface OrganizzazioneService {
	
	public void inserisciOrganizzazione (OrganizzazioneCreateDTO createDto);
	
	public void modificaOrganizzazione (UUID uuid, OrganizzazioneUpdateDTO updateDto);
	
	public void eliminaOrganizzazione (UUID uuid);
	
	public OrganizzazioneResponseDTO cercaPerEmail (String email);
	
	public OrganizzazioneResponseDTO cercaPerToken (int token);
	
	public OrganizzazioneResponseDTO cercaPerUUID (UUID uuid);
	
	public List<InvitoResponseDTO> cercaInvitiOrganizzazione (UUID uuid);
	
	public List<EventoResponseDTO> cercaEventiOrganizzazione (UUID uuid);
	
	public List<UtenteResponseDTO> cercaUtentiOrganizzazione (UUID uuid);

}

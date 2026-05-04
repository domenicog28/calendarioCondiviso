package com.webapp.calendarioCondiviso.evento;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.utente.Utente;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
	
	List<Evento> findByOrganizzazione (Organizzazione organizzazione);
	
	List<Evento> findByUtente (Utente utente);
	
	List<Evento> findByOrganizzazioneAndTipo (Organizzazione organizzazione, TipoEvento tipo);
	
	List<Evento> findByTimeNotificaBeforeAndNotificatoFalse(ZonedDateTime time);
}

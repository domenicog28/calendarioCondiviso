package com.webapp.calendarioCondiviso.invito;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;

public interface InvitoRepository extends JpaRepository<Invito, UUID> {
	
	List <Invito> findByOrganizzazione (Organizzazione organizzazione);
	
	
}

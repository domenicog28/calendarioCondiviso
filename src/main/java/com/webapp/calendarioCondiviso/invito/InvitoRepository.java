package com.webapp.calendarioCondiviso.invito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;

@Repository
public interface InvitoRepository extends JpaRepository<Invito, UUID> {
	
	Optional<Invito> findByIdToken(UUID uuid);
	
	List <Invito> findByOrganizzazione (Organizzazione organizzazione);
	
	
}

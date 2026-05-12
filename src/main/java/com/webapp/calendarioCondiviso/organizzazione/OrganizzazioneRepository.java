package com.webapp.calendarioCondiviso.organizzazione;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrganizzazioneRepository extends JpaRepository<Organizzazione, UUID> {
	
	Optional<Organizzazione> findByEmail (String email);
	
	Optional<Organizzazione> findByTokenVerifica (int token);
	
}

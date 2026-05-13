package com.webapp.calendarioCondiviso.utente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
	
	Optional<Utente> findByEmail(String email);
	
	Optional<Utente> findByTokenVerifica(int token);
	
	List<Utente> findByOrganizzazione (Organizzazione organizzazione);

}

package com.webapp.calendarioCondiviso.utente;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
	
	Optional<Utente> findByEmail(String email);
	
	Optional<Utente> findByTokenVerifica(int token);

}

package com.webapp.calendarioCondiviso.utente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
	
	Optional<Utente> findByEmail(String email);
	
	Optional<Utente> findByTokenVerifica(int token);
	
	List<Utente> findByOrganizzazione (Organizzazione organizzazione);
	
	@Modifying
	@Transactional
	@Query ("update Utente u set u.verificato = true where u.email = :email")
	void impostaVerificato(@Param ("email")String email);

}

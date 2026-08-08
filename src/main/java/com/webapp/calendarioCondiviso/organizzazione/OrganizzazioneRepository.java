package com.webapp.calendarioCondiviso.organizzazione;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrganizzazioneRepository extends JpaRepository<Organizzazione, UUID> {
	
	Optional<Organizzazione> findByEmail (String email);
	
	Optional<Organizzazione> findByTokenVerifica (int token);
	
	@Modifying
	@Query ("update Organizzazione o set o.verificata = true where o.email = :email")
	void impostaVerificato(@Param ("email")String email);
	
}

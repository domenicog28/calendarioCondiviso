package com.webapp.calendarioCondiviso.organizzazione;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.webapp.calendarioCondiviso.utente.Utente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="organizzazione")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizzazione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID idOrganizzazione;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
	private String passwordHash;
	
	@Column(nullable=false)
	private String descrizione;
	
	private boolean verificata;
	
	private int tokenVerifica;
	
	private ZonedDateTime scadenzaToken;
	
	
	@OneToMany (mappedBy ="organizzazione", fetch=FetchType.LAZY)
	private List<Utente> utenti;
}

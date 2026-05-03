package com.webapp.calendarioCondiviso.organizzazione;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.webapp.calendarioCondiviso.evento.Evento;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="organizzazione")
@Getter
@Setter
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
	
	@Builder.Default
	private boolean verificata = false;
	
	private int tokenVerifica;
	
	private ZonedDateTime scadenzaToken;
	
	
	@OneToMany(mappedBy ="organizzazione", fetch=FetchType.LAZY)
	private List<Utente> utenti;
	
	@OneToMany(mappedBy ="organizzazione", fetch=FetchType.LAZY)
	private List<Evento> eventi;
}

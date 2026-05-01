package com.webapp.calendarioCondiviso.utente;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.webapp.calendarioCondiviso.evento.Evento;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name="utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID idUtente;
	
	@ManyToOne
	@JoinColumn(name="id_organizzazione", nullable=false)
	private Organizzazione organizzazione;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
	private String passwordHash;
	
	@Column(nullable=false, length=50)
	private String nome;
	
	@Column(nullable=false, length=50)
	private String cognome;
	
	private boolean verificato = false;
	
	private int tokenVerifica;
	
	private ZonedDateTime scadenzaToken; 
	
	@OneToMany(mappedBy="utente", fetch=FetchType.LAZY)
	private List<Evento> eventi;
}

package com.webapp.calendarioCondiviso.evento;


import java.time.ZonedDateTime;
import java.util.UUID;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.utente.Utente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="evento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID idEvento;
	
	@ManyToOne
	@JoinColumn(name="id_organizzazione", nullable = false)
	private Organizzazione organizzazione;
	
	@ManyToOne
	@JoinColumn(name="id_utente")
	private Utente utente;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoEvento tipo;
	
	@Column(length = 50)
	private String titolo;
	
	@Column(length = 255)
	private String descrizione;
	
	@Column(nullable = false)
	private ZonedDateTime timeInizio;
	
	@Column(nullable = false)
	private ZonedDateTime timeFine;
	
	@Column(nullable = false)
	private ZonedDateTime timeNotifica;
	
	private boolean notificato = false;
	
}

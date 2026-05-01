package com.webapp.calendarioCondiviso.invito;


import java.time.ZonedDateTime;
import java.util.UUID;

import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*CREATE TABLE invito(id_token UUID PRIMARY KEY, id_organizzazione UUID NOT NULL REFERENCES organizzazione (id_organizzazione), time_generato TIMESTAMP WITH TIME ZONE NOT NULL, scadenza_token
TIMESTAMP WITH TIME ZONE NOT NULL);*/

@Entity
@Table(name="invito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invito {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID idToken;
	
	@ManyToOne
	@JoinColumn(name="id_organizzazione", nullable = false)
	private Organizzazione organizzazione;
	
	@Column(nullable = false)
	private ZonedDateTime timeGenerato;
	
	@Column(nullable = false)
	private ZonedDateTime scadenzaToken;
}

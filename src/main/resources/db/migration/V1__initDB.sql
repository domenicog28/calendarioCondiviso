CREATE TABLE organizzazione(id_organizzazione UUID PRIMARY KEY, email VARCHAR(255) NOT NULL, password_hash VARCHAR(255) NOT NULL, descrizione VARCHAR(255) NOT NULL, verificata BOOLEAN DEFAULT false, token_verifica INTEGER, scadenza_token TIMESTAMP WITH TIME ZONE, CONSTRAINT organizzazione_email_unica UNIQUE (email));

CREATE TABLE utente(id_utente UUID PRIMARY KEY, id_organizzazione UUID NOT NULL REFERENCES organizzazione (id_organizzazione), email VARCHAR(255) NOT NULL, password_hash VARCHAR(255) NOT NULL, nome VARCHAR(50) NOT NULL, cognome VARCHAR(50) NOT NULL, verificato BOOLEAN DEFAULT false, token_verifica INTEGER, scadenza_token TIMESTAMP WITH TIME ZONE, CONSTRAINT utente_email_unica UNIQUE(email));

CREATE TABLE evento(id_evento UUID PRIMARY KEY, id_organizzazione UUID NOT NULL REFERENCES organizzazione (id_organizzazione), id_utente UUID REFERENCES utente (id_utente), tipo VARCHAR(50) NOT NULL, titolo VARCHAR(50), descrizione VARCHAR(255), time_inizio TIMESTAMP WITH TIME ZONE NOT NULL, time_fine TIMESTAMP WITH TIME ZONE NOT NULL, time_notifica TIMESTAMP WITH TIME ZONE NOT NULL, notificato BOOLEAN DEFAULT false, CHECK(tipo='TEAM' OR id_utente IS NOT NULL));

CREATE TABLE invito(id_token UUID PRIMARY KEY, id_organizzazione UUID NOT NULL REFERENCES organizzazione (id_organizzazione), time_generato TIMESTAMP WITH TIME ZONE NOT NULL, scadenza_token
TIMESTAMP WITH TIME ZONE NOT NULL);

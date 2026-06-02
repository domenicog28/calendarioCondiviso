package com.webapp.calendarioCondiviso;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.webapp.calendarioCondiviso.evento.EventoControllerTest;
import com.webapp.calendarioCondiviso.invito.InvitoControllerTest;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneControllerTest;
import com.webapp.calendarioCondiviso.utente.UtenteControllerTest;

@Suite
@SelectClasses({ 
	OrganizzazioneControllerTest.class,
	UtenteControllerTest.class,
	EventoControllerTest.class,
	InvitoControllerTest.class })
public class ControllerTestSuite {

}

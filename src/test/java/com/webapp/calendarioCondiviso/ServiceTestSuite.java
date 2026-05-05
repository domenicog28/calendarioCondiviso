package com.webapp.calendarioCondiviso;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.webapp.calendarioCondiviso.evento.EventoServiceTest;
import com.webapp.calendarioCondiviso.invito.InvitoServiceTest;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneServiceTest;
import com.webapp.calendarioCondiviso.utente.UtenteServiceTest;



@Suite
@SelectClasses({
	OrganizzazioneServiceTest.class,
	UtenteServiceTest.class,
	EventoServiceTest.class,
	InvitoServiceTest.class
})
public class ServiceTestSuite {

}

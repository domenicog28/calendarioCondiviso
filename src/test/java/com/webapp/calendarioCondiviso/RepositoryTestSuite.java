package com.webapp.calendarioCondiviso;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.webapp.calendarioCondiviso.evento.EventoRepositoryTest;
import com.webapp.calendarioCondiviso.invito.InvitoRepositoryTest;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepositoryTest;
import com.webapp.calendarioCondiviso.utente.UtenteRepositoryTest;

@Suite
@SelectClasses({
	OrganizzazioneRepositoryTest.class,
	UtenteRepositoryTest.class,
	EventoRepositoryTest.class,
	InvitoRepositoryTest.class
})
public class RepositoryTestSuite {

}

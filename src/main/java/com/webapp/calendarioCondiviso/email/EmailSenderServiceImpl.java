package com.webapp.calendarioCondiviso.email;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.webapp.calendarioCondiviso.auth.jwt.JwtService;
import com.webapp.calendarioCondiviso.auth.jwt.Ruolo;
import com.webapp.calendarioCondiviso.exception.CodiceVerificaErratoException;
import com.webapp.calendarioCondiviso.exception.ResourceNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	OrganizzazioneRepository organizzazioneRepository;
	
	@Autowired 
	UtenteRepository utenteRepository;
	
	@Autowired
	JwtService jwtService;

	@Override
	public void sendTokenEmail(String to, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("Verifica Email - CalendarioCondiviso");
		message.setText("Benvenuto in Calendario Condiviso.\nIl token per la verifica dell'email è: \n"+token);
		
		mailSender.send(message);

	}
	
	@Override
	public String[] modificaVerificaEmail (String email, String codiceVerifica) {
		
		String[] token = new String [2];
		
		Optional<Organizzazione> organizzazione = organizzazioneRepository.findByEmail(email);
		
		if (organizzazione.isPresent()) {
			
			Organizzazione org = organizzazione.get();
			
			if (Integer.toString(org.getTokenVerifica()).equals(codiceVerifica)){
				
				organizzazioneRepository.impostaVerificato(email);
				
				
				
				token[0] = jwtService.generateAccessToken(org.getIdOrganizzazione(), Ruolo.ORGANIZZAZIONE);
				token[1] = jwtService.generateRefreshToken(org.getIdOrganizzazione());
				
				return token;
				
			} else {
				
				throw new CodiceVerificaErratoException("Codice verifica email errato");
				
			}
			
		} else {
			
			Optional<Utente> utente = utenteRepository.findByEmail(email);
			
			if (utente.isPresent()) {
				//da modificare dopo aver aggiunto la query di impostaVerificato su UtenteRepository
				
				System.out.println("Utente Presente");
				
				return token;
				
			} else {
				
				throw new ResourceNotFoundException("errore");
				
			}
			
		}
		
	}

}

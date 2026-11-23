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
		
		Optional<Organizzazione> org = organizzazioneRepository.findByEmail(email);
		
		if (org.isPresent()) {
			
			Organizzazione organizzazione = org.get();
			
			if (organizzazione.getTokenVerifica() == Integer.parseInt(codiceVerifica)) {
				
				organizzazioneRepository.impostaVerificato(email);
				
				token[0] = jwtService.generateAccessToken(organizzazione.getIdOrganizzazione(), Ruolo.ORGANIZZAZIONE);
				token[1] = jwtService.generateRefreshToken(organizzazione.getIdOrganizzazione());
				
				return token;
				
			} else {
				
				throw new CodiceVerificaErratoException("Codice verifica email errato");
				
			}
			
		} else {
			
			Optional<Utente> ut = utenteRepository.findByEmail(email);
			
			if (ut.isPresent()) {
				
				Utente utente = ut.get();
				
				if(utente.getTokenVerifica() == Integer.parseInt(codiceVerifica)) {
					
					utenteRepository.impostaVerificato(email);
					
					token[0] = jwtService.generateAccessToken(utente.getIdUtente(), Ruolo.UTENTE);
					token[1] = jwtService.generateRefreshToken(utente.getIdUtente());
					
					return token;
					
				} else {
					
					throw new CodiceVerificaErratoException("Codice verifica email errato");
					
				}
				
			} else {
				
				throw new ResourceNotFoundException("L'email inserita non è presente nel database!");
				
			}
			
		}
		
	}

}

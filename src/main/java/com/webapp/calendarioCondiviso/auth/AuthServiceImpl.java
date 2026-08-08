package com.webapp.calendarioCondiviso.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.calendarioCondiviso.auth.jwt.JwtService;
import com.webapp.calendarioCondiviso.auth.jwt.Ruolo;
//import com.webapp.calendarioCondiviso.exception.CodiceVerificaErratoException;
import com.webapp.calendarioCondiviso.exception.EmailNonVerificataException;
import com.webapp.calendarioCondiviso.exception.InvalidCredentialsException;
//import com.webapp.calendarioCondiviso.exception.ResourceNotFoundException;
import com.webapp.calendarioCondiviso.organizzazione.Organizzazione;
import com.webapp.calendarioCondiviso.organizzazione.OrganizzazioneRepository;
import com.webapp.calendarioCondiviso.utente.Utente;
import com.webapp.calendarioCondiviso.utente.UtenteRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	OrganizzazioneRepository organizzazioneRepository;

	@Autowired
	UtenteRepository utenteRepository;

	@Autowired
	JwtService jwtService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public String[] login(Credenziali credenziali) {

		String[] token = new String[2];

		Optional<Organizzazione> organizzazione = organizzazioneRepository.findByEmail(credenziali.email());

		if (organizzazione.isPresent()) {
			
			if (!organizzazione.get().isVerificata()) {
				
				throw new EmailNonVerificataException("Email non verificata!");
				
			} else {

				Organizzazione org = organizzazione.get();
	
				if (passwordEncoder.matches(credenziali.password(), org.getPasswordHash())) {
	
					token[0] = jwtService.generateAccessToken(org.getIdOrganizzazione(), Ruolo.ORGANIZZAZIONE);
					token[1] = jwtService.generateRefreshToken(org.getIdOrganizzazione());
	
					return token;
	
				} else {
	
					throw new InvalidCredentialsException("Le credenziali inserite non sono corrette!");
	
				}
			}

		} else {

			Optional<Utente> utente = utenteRepository.findByEmail(credenziali.email());

			if (utente.isPresent()) {

				Utente ut = utente.get();

				if (passwordEncoder.matches(credenziali.password(), ut.getPasswordHash())) {

					token[0] = jwtService.generateAccessToken(ut.getIdUtente(), Ruolo.UTENTE,
							ut.getOrganizzazione().getIdOrganizzazione());
					token[1] = jwtService.generateRefreshToken(ut.getIdUtente());

					return token;
				} else {

					throw new InvalidCredentialsException("Le credenziali inserite non sono corrette!");

				}

			} else {

				throw new InvalidCredentialsException("Le credenziali inserite non sono corrette!");

			}

		}

	}

	@Override
	public String refresh(String refreshToken) {

		if (!jwtService.validateToken(refreshToken)) {
			
			throw new InvalidCredentialsException ("Refresh token scaduto!");
			
		} else {

			String uuid = jwtService.estraiUUID(refreshToken);

			Optional<Organizzazione> organizzazione = organizzazioneRepository.findById(UUID.fromString(uuid));

			if (organizzazione.isPresent()) {

				Organizzazione org = organizzazione.get();

				return jwtService.generateAccessToken(org.getIdOrganizzazione(), Ruolo.ORGANIZZAZIONE);

			} else {

				Optional<Utente> utente = utenteRepository.findById(UUID.fromString(uuid));

				if (utente.isPresent()) {

					Utente ut = utente.get();

					return jwtService.generateAccessToken(ut.getIdUtente(), Ruolo.UTENTE,
							ut.getOrganizzazione().getIdOrganizzazione());
					
				} else {
					
					throw new InvalidCredentialsException("Token non valido!");
				}
					
			}
				
		}
	}
	/*
	@Override
	public String[] modificaVerificaEmail (UUID uuid, String codiceVerifica) {
		
		String[] token = new String [2];
		
		Optional<Organizzazione> organizzazione = organizzazioneRepository.findById(uuid);
		
		if (organizzazione.isPresent()) {
			
			Organizzazione org = organizzazione.get();
			
			if (Integer.toString(org.getTokenVerifica()).equals(codiceVerifica)){
				
				organizzazioneRepository.impostaVerificato(uuid);
				
				
				
				token[0] = jwtService.generateAccessToken(org.getIdOrganizzazione(), Ruolo.ORGANIZZAZIONE);
				token[1] = jwtService.generateRefreshToken(org.getIdOrganizzazione());
				
				return token;
				
			} else {
				
				throw new CodiceVerificaErratoException("Codice verifica email errato");
				
			}
			
		} else {
			
			Optional<Utente> utente = utenteRepository.findById(uuid);
			
			if (utente.isPresent()) {
				//da modificare dopo aver aggiunto la query di impostaVerificato su UtenteRepository
				
				System.out.println("Utente Presente");
				
				return token;
				
			} else {
				
				throw new ResourceNotFoundException("errore");
				
			}
			
		}
		
	}
	*/
	
	
	
}

				



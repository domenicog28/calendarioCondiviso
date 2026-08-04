package com.webapp.calendarioCondiviso.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendTokenEmail(String to, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("Verifica Email - CalendarioCondiviso");
		message.setText("Benvenuto in Calendario Condiviso.\nIl token per la verifica dell'email è: \n"+token);
		
		mailSender.send(message);

	}

}

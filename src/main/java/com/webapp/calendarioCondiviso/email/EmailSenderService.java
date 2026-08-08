package com.webapp.calendarioCondiviso.email;

public interface EmailSenderService{
	
	void sendTokenEmail (String to, String token);
	
	public String[] modificaVerificaEmail (String email, String codiceVerifica);
}

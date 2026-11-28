package com.webapp.calendarioCondiviso.email;

public interface EmailSenderService{
	
	public void sendTokenEmail (String to, String token);
	
	public String[] modificaVerificaEmail (String email, String codiceVerifica);
	
	public Boolean sendNewToken (String to);
}

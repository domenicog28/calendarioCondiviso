package com.webapp.calendarioCondiviso.email;


public interface EmailSenderService{
	
	void sendTokenEmail (String to, String token);
	
}

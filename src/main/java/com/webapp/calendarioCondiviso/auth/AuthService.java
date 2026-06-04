package com.webapp.calendarioCondiviso.auth;

public interface AuthService {
	
	public String[] login(Credenziali credenziali);
	
	public String refresh(String refreshToken);
	

}

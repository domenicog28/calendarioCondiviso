package com.webapp.calendarioCondiviso.auth;

//import java.util.UUID;

public interface AuthService {
	
	public String[] login(Credenziali credenziali);
	
	public String refresh(String refreshToken);
	
	//public String[] modificaVerificaEmail (UUID uuid, String codiceVerifica);
	

}

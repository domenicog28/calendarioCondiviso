package com.webapp.calendarioCondiviso.auth;

import org.springframework.context.annotation.Profile;

//import java.util.UUID;

@Profile("!test")
public interface AuthService {
	
	public String[] login(Credenziali credenziali);
	
	public String refresh(String refreshToken);

}

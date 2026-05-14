package com.webapp.calendarioCondiviso;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

public class TokenUtils {
	
	public static int generaToken() {
		Random random = new Random();
		int numero = 100000 + random.nextInt(900000);
		return numero;
	}
	
	public static ZonedDateTime generaScadenzaToken() {
		ZonedDateTime scadenza = ZonedDateTime.now(ZoneId.of("Europe/Rome")).plusDays(7);
		return scadenza;
	}

}

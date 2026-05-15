package com.webapp.calendarioCondiviso.invito;

import java.util.UUID;

import com.webapp.calendarioCondiviso.invito.dto.InvitoCreateDTO;
import com.webapp.calendarioCondiviso.invito.dto.InvitoResponseDTO;

public interface InvitoService {
	
	public void inserisciInvito (InvitoCreateDTO createDto);
	
	public InvitoResponseDTO verificaToken(UUID uuid);
	
	public void eliminaInvito (UUID uuid);

}

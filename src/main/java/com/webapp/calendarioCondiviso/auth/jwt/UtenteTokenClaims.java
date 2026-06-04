package com.webapp.calendarioCondiviso.auth.jwt;

import java.util.UUID;

public record UtenteTokenClaims (UUID uuidUtente, Ruolo ruolo, UUID uuidOrganizzazione) implements TokenClaims{}

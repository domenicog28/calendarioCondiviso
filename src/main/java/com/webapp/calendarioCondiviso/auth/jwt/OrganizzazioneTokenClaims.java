package com.webapp.calendarioCondiviso.auth.jwt;

import java.util.UUID;

public record OrganizzazioneTokenClaims (UUID uuidOrganizzazione, Ruolo ruolo) implements TokenClaims {}

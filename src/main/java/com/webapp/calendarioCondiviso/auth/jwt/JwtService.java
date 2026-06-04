package com.webapp.calendarioCondiviso.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private final Key key;
	private final int accessTokenExpiredMinuti;
	private final int refreshTokenExpiredMinuti;
	
	public JwtService (@Value("${jwt.secret}")String chiaveSegreta,@Value("${jwt.accessTokenExp}")int accessTokenExpireMinuti, @Value ("${jwt.refreshTokenExp}")int refreshTokenExpireMinuti) {
		
		this.accessTokenExpiredMinuti = accessTokenExpireMinuti;
		this.refreshTokenExpiredMinuti = refreshTokenExpireMinuti;
		
		this.key = Keys.hmacShaKeyFor(chiaveSegreta.getBytes(StandardCharsets.UTF_8));
		
	}
	
	public String generateAccessToken (UUID uuid, Ruolo ruolo) {
		
		Instant now = Instant.now();
		Instant expire = now.plus(accessTokenExpiredMinuti, ChronoUnit.MINUTES);
		
		return Jwts.builder()
				.subject(uuid.toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(expire))
				.claim("ruolo", ruolo.toString())
				.signWith(key)
				.compact();
	}
	
	public String generateAccessToken (UUID uuid, Ruolo ruolo, UUID idOrganizzazione) {
		
		Instant now = Instant.now();
		Instant expire = now.plus(accessTokenExpiredMinuti, ChronoUnit.MINUTES);
		
		return Jwts.builder()
				.subject(uuid.toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(expire))
				.claim("ruolo", ruolo.toString())
				.claim("idOrganizzazione", idOrganizzazione.toString())
				.signWith(key)
				.compact();
		
	}
	
	
	
	public String generateRefreshToken (UUID uuid) {
		Instant now = Instant.now();
		Instant expire = now.plus(refreshTokenExpiredMinuti, ChronoUnit.MINUTES);
		
		return Jwts.builder()
				.subject(uuid.toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(expire))
				.signWith(key)
				.compact();
	}
	
	public boolean validateToken(String token) {
		
		try {
			
			parseToken(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public TokenClaims claimsToken(String token) {
		
		Claims claims = parseToken(token);
		
		if (claims.get("ruolo", String.class).equals((Ruolo.ORGANIZZAZIONE).toString())) {
			
			OrganizzazioneTokenClaims tokenClaims = new OrganizzazioneTokenClaims(UUID.fromString(claims.getSubject()), Ruolo.ORGANIZZAZIONE);
			
			return tokenClaims;
			
		} else {
			
			UtenteTokenClaims tokenClaims = new UtenteTokenClaims(UUID.fromString(claims.getSubject()), Ruolo.UTENTE, UUID.fromString(claims.get("idOrganizzazione", String.class)));
			
			return tokenClaims;
			
		}
	}
	
	private Claims parseToken(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public String estraiUUID (String token) {
		return parseToken(token).getSubject();
	}
	

}

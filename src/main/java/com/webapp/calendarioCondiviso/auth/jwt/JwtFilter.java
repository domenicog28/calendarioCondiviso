package com.webapp.calendarioCondiviso.auth.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		

	    String path = request.getRequestURI();
	    
	    if (path.startsWith("/auth/") || 
	    	    path.equals("/organizzazioni/registrazione") ||
	    	    path.startsWith("/utenti/registrazione/")) {
	    	    filterChain.doFilter(request, response);
	    	    return;
	    	}
		
		String accessBearerToken = request.getHeader("Authorization");
		String accessToken = "";
		
		if (StringUtils.hasText(accessBearerToken) && accessBearerToken.startsWith("Bearer ")) {
			
			accessToken = accessBearerToken.substring(7, accessBearerToken.length());
			
		} else {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token non valido!");
			return;
			
		}
		
		if (StringUtils.hasText(accessToken) && jwtService.validateToken(accessToken)) {
			
			TokenClaims claims = jwtService.claimsToken(accessToken);
			
			UsernamePasswordAuthenticationToken autenticazione = new UsernamePasswordAuthenticationToken(claims, null,List.of());
			
			SecurityContextHolder.getContext().setAuthentication(autenticazione);
			
			filterChain.doFilter(request, response);
			
		} else {
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Token non valido!");
			return;
			
		}

	}

}

package com.webapp.calendarioCondiviso.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<String> notFoundHandler(Exception ex){
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		
	}
	
	@ExceptionHandler(DuplicateEmailException.class)
	public final ResponseEntity<String> duplicateEmailHandler(Exception ex){
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		
	}
	
	@ExceptionHandler(ExpiredTokenException.class)
	public final ResponseEntity<String> expiredTokenHandler(Exception ex){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public final ResponseEntity<String> invalidCredentialsHandler(Exception ex){
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
		
	}
	
	@ExceptionHandler(EmailNonVerificataException.class)
	public final ResponseEntity<String> emailNonVerificataHandler (Exception ex){
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
	
	@ExceptionHandler(CodiceVerificaErratoException.class)
	public final ResponseEntity<String> codiceVerificaErratoHandler (Exception ex){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

}

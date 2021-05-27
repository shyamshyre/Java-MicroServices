package com.devhat.estore.ProductService.core.errorhandling;

import java.util.Date;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ProductServiceErrorHandler {

	// specify the specific exception you want to handle
	@ExceptionHandler(value= {IllegalStateException.class})
	public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request){
		ErrorMessage  errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	// Handling all the exceptions from the command separately.
	@ExceptionHandler(value= {CommandExecutionException.class})
	public ResponseEntity<Object> handleOtherException(CommandExecutionException cex, WebRequest request){
		ErrorMessage errorMessage = new ErrorMessage(new Date(), cex.getMessage());
		return new ResponseEntity<>(errorMessage, new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	// Handling all the exceptions from the command separately.
		@ExceptionHandler(value= {Exception.class})
		public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request){
			ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
			return new ResponseEntity<>(errorMessage, new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
}

package com.cinebook.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = DuplicateResourceException.class)
	public ResponseEntity<?> handleDuplicate(DuplicateResourceException ex) {

		Map<String, Object> error = new HashMap<String, Object>();
		error.put("Timestamp", LocalDateTime.now());
		error.put("error", "Duplicate error");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex){
		
		Map<String,Object> error = new HashMap<String, Object>();
		error.put("Timestamp", LocalDateTime.now());
		error.put("error", "Duplicate error");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		
	}
	

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleRuntimeException(Exception ex) {

		Map<String, Object> error = new HashMap<String, Object>();
		error.put("Timestamp", LocalDateTime.now());
		error.put("error", "Internal server error");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, Object> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

}

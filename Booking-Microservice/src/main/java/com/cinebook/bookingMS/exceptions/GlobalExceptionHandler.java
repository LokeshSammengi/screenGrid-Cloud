package com.cinebook.bookingMS.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, Object> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SeatLockedException.class)
    public ResponseEntity<ApiError> handleSeatLocked(
            RegistrationNotDoneException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NO_CONTENT.value(),
                "NOT_AVAIABLE",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	@ExceptionHandler(NoChangesFoundException.class)
    public ResponseEntity<ApiError> handleNoChangesFoundException(
            NoChangesFoundException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_MODIFIED.value(),
                "NOT_MODIFIED",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_MODIFIED);
    }
	
	@ExceptionHandler(RegistrationNotDoneException.class)
    public ResponseEntity<ApiError> handleRegistrationNotDone(
            RegistrationNotDoneException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_IMPLEMENTED.value(),
                "NOT_IMPLEMENTED",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	 @ExceptionHandler(DuplicateResourceException.class)
	    public ResponseEntity<ApiError> handleDuplicateResource(
	            DuplicateResourceException ex,
	            HttpServletRequest request) {

	        ApiError error = new ApiError(
	                LocalDateTime.now(),
	                HttpStatus.CONFLICT.value(),
	                "CONFLICT",
	                ex.getMessage(),
	                request.getRequestURI()
	        );

	        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	    }
	 
	 @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiError> handleGlobalException(
	            Exception ex,
	            HttpServletRequest request) {

	        ApiError error = new ApiError(
	                LocalDateTime.now(),
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                "INTERNAL_SERVER_ERROR",
	                ex.getMessage(),
	                request.getRequestURI()
	        );

	        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	    }


}

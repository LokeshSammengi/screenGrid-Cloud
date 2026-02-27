package com.cinebook.payment.exceptions;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}
}

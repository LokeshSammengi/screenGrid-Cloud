package com.cinebook.exception;

@SuppressWarnings("serial")
public class RegistrationNotDoneException extends RuntimeException {
	
	public RegistrationNotDoneException(String msg) {
		super(msg);
	}

}

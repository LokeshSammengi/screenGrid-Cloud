package com.cinebook.exception;

@SuppressWarnings("serial")
public class NoChangesFoundException extends RuntimeException {
	
	public NoChangesFoundException(String message){
		super(message);
	}

}

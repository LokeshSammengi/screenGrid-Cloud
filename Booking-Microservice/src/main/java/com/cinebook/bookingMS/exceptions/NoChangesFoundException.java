package com.cinebook.bookingMS.exceptions;

@SuppressWarnings("serial")
public class NoChangesFoundException extends RuntimeException {
	
	public NoChangesFoundException(String message){
		super(message);
	}

}

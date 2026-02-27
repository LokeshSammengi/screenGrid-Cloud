package com.cinebook.bookingMS.exceptions;

public class SeatLockedException extends RuntimeException {

	public SeatLockedException(String message) {
		super(message);
	}
}

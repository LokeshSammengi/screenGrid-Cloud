package com.cinebook.bookingMS.service;

import java.util.List;

import com.cinebook.bookingMS.VO.BookingResponse;
import com.cinebook.bookingMS.entities.Booking;
import com.cinebook.bookingMS.enums.BookingStatus;

public interface BookingService {

	public String createBooking(Long userId, Long showId, List<Long> seatIds);
	public void confirmBooking(Long bookingId);
	public void cancelBooking(Long bookingId);
	
	public BookingResponse getBookingById(Long bookingId);
	public List<Booking> getAllBookings();
	
	public void updateBookingStatus(Long bookingId, BookingStatus status);
	
//	public void removeBookings(Long bookingId);
//	
//	public Booking validateBookingForPayment(Long bookingId);
//	public Booking markPaymentStarted(Long bookingId);
//	public void handlePaymentFailure(Long bookingId);
							
	
}

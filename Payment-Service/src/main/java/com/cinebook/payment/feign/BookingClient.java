package com.cinebook.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cinebook.payment.VO.BookingResponse;

@FeignClient(name = "Booking-Microservice")
public interface BookingClient {

	@PostMapping("/bookings/confirm/{bookingId}")
	void confirmBooking(@PathVariable Long bookingId);

	@PostMapping("/bookings/cancel/{bookingId}")
	void cancelBooking(@PathVariable Long bookingId);

	@GetMapping("/bookings/GetBookingById/{bookingId}")
    public BookingResponse getBookingById(
            @PathVariable("bookingId") Long bookingId);

}
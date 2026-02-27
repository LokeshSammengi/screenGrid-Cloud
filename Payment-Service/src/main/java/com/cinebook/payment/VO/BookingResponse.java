package com.cinebook.payment.VO;

import java.util.List;

import com.cinebook.payment.enums.BookingStatus;

import lombok.Data;

@Data
public class BookingResponse {

	private Long userId;
	private Long showId;
	private Double totalAmount;
	private BookingStatus bookingStatus;
	private List<ShowSeatResponseDto> Seats;

}
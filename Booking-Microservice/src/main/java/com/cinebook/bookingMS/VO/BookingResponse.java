package com.cinebook.bookingMS.VO;

import java.util.List;

import com.cinebook.bookingMS.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

	private Long userId;
	private Long showId;
	private Double totalAmount;
	private BookingStatus bookingStatus;
	private List<ShowSeatResponseDTO> Seats;

}

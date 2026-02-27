package com.cinebook.bookingMS.service;

import java.util.List;

import com.cinebook.bookingMS.entities.ShowSeat;

public interface ShowSeatService {

	public void generateSeatsForShow(Long showId, Long screenId, Double basePrice);

	public List<ShowSeat> getAvailableSeats(Long showId);

	public void confirmSeats(Long showId, List<Long> seatIds);

	public void releaseSeats(Long showId, List<Long> seatIds);

	public void lockSeats(Long showId, List<Long> seatIds);

	public List<ShowSeat> getSeatsByShowId(Long showId);

}

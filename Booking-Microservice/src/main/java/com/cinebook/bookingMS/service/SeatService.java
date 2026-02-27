package com.cinebook.bookingMS.service;

import java.util.List;

import com.cinebook.bookingMS.VO.SingleSeatRequest;
import com.cinebook.bookingMS.entities.Seat;

public interface SeatService {

	public Seat createSeat(SingleSeatRequest request);
	public List<Seat> createBulkSeat(List<SingleSeatRequest> bulkRequest);
	
	public Seat getSeatById(Long id);
	public List<Seat> getSeatsByScreenId(Long screenId);
	
}

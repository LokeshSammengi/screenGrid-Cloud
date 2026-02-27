package com.cinebook.bookingMS.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinebook.bookingMS.entities.Seat;
import com.cinebook.bookingMS.enums.SeatType;

public interface SeatRepository extends JpaRepository<Seat,Long> {

	List<Seat> findByScreenId(Long screenId);

	boolean existsByScreenIdAndSeatNumber(Long screenId, String seatNumber);

}

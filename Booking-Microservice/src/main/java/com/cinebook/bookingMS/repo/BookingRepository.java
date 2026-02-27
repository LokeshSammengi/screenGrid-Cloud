package com.cinebook.bookingMS.repo;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cinebook.bookingMS.entities.Booking;
import com.cinebook.bookingMS.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByShowId(Long showId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

	List<Booking> findByBookingStatusAndExpiryTimeBefore(BookingStatus pending, LocalDateTime now);
}
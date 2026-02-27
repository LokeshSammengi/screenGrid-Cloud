package com.cinebook.bookingMS.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cinebook.bookingMS.entities.Booking;
import com.cinebook.bookingMS.entities.ShowSeat;
import com.cinebook.bookingMS.enums.BookingStatus;
import com.cinebook.bookingMS.enums.SeatStatus;
import com.cinebook.bookingMS.repo.BookingRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepo;

    @Scheduled(fixedRate = 60000) // every 1 min
    @Transactional
    public void expirePendingBookings() {

        List<Booking> expiredBookings =
                bookingRepo.findByBookingStatusAndExpiryTimeBefore(
                        BookingStatus.PENDING,
                        LocalDateTime.now()
                );

        for (Booking booking : expiredBookings) {

            // release seats
            for (ShowSeat seat : booking.getShowSeats()) {
                seat.setSeatStatus(SeatStatus.AVAILABLE);
                seat.setLockedAt(null);
                seat.setBooking(null);
            }

            booking.setBookingStatus(BookingStatus.EXPIRED);
        }
    }
}
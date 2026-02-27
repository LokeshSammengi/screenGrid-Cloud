package com.cinebook.bookingMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinebook.bookingMS.VO.BookingResponse;
import com.cinebook.bookingMS.enums.BookingStatus;
import com.cinebook.bookingMS.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /* ============================================================
       1️⃣ CREATE BOOKING  (PUBLIC API - Called by Frontend)

       Flow:
       - Locks seats
       - Creates booking (PENDING)
       - Calls Payment Service
       - Returns checkout URL
    ============================================================ */
    @PostMapping("/create")
    public ResponseEntity<String> createBooking(
            @RequestParam Long userId,
            @RequestParam Long showId,
            @RequestParam List<Long> seatIds) {

        String checkoutUrl =
                bookingService.createBooking(userId, showId, seatIds);

        return ResponseEntity.ok(checkoutUrl);
    }

    /* ============================================================
       2️⃣ CONFIRM BOOKING (INTERNAL API - Called by Payment Service)

       Triggered when payment is SUCCESS.
       - Marks seats as BOOKED
       - Changes booking status to CONFIRMED

       ⚠ Should NOT be exposed to frontend directly.
       Secure this endpoint (API key / internal network).
    ============================================================ */
    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<Void> confirmBooking(
            @PathVariable Long bookingId) {

        bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    /* ============================================================
       3️⃣ CANCEL BOOKING (Internal / Manual Cancel)

       Used for:
       - Payment failure callback
       - Admin cancellation
       - Manual cancellation (if allowed)
    ============================================================ */
    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long bookingId) {

        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    /* ============================================================
       4️⃣ GET BOOKING BY ID (Frontend - User Dashboard)

       Returns:
       - Booking details
       - Seat details
       - Status
    ============================================================ */
    @GetMapping("/GetBookingById/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId) {

        BookingResponse response =
                bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(response);
    }

    /* ============================================================
       5️⃣ GET ALL BOOKINGS (ADMIN ONLY)

       Used for:
       - Admin dashboard
       - Reporting
    ============================================================ */
    @GetMapping("/admin/all")
    public ResponseEntity<List<?>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    /* ============================================================
       6️⃣ UPDATE BOOKING STATUS (ADMIN / INTERNAL)

       Only for special cases.
       Normally status changes via:
       - confirmBooking()
       - cancelBooking()
       - scheduler
    ============================================================ */
    @PutMapping("/admin/{bookingId}/status")
    public ResponseEntity<String> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status) {

        bookingService.updateBookingStatus(bookingId, status);

        return ResponseEntity.ok("Booking status updated");
    }
}
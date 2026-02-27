package com.cinebook.bookingMS.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinebook.bookingMS.VO.SingleSeatRequest;
import com.cinebook.bookingMS.entities.Seat;
import com.cinebook.bookingMS.service.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // Create single seat
    @PostMapping
    public ResponseEntity<Seat> createSeat(
            @RequestBody SingleSeatRequest request) {

        Seat seat = seatService.createSeat(request);
        return new ResponseEntity<>(seat, HttpStatus.CREATED);
    }

    // Create bulk seats
    @PostMapping("/bulk")
    public ResponseEntity<List<Seat>> createBulkSeats(
            @RequestBody List<SingleSeatRequest> requests) {

        List<Seat> seats = seatService.createBulkSeat(requests);
        return new ResponseEntity<>(seats, HttpStatus.CREATED);
    }

    // Get seat by id
    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {

        Seat seat = seatService.getSeatById(id);
        return ResponseEntity.ok(seat);
    }

    // Get all seats by screenId
    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<Seat>> getSeatsByScreenId(
            @PathVariable Long screenId) {

        List<Seat> seats = seatService.getSeatsByScreenId(screenId);
        return ResponseEntity.ok(seats);
    }
}
package com.cinebook.bookingMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinebook.bookingMS.VO.ShowSeatResponseDTO;
import com.cinebook.bookingMS.service.ShowSeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shows/{showId}/seats")
@RequiredArgsConstructor
public class ShowSeatController {

	private final ShowSeatService showSeatService;
	
	@PostMapping("/generate")
	public ResponseEntity<String> generateSeats(
	        @PathVariable Long showId,
	        @RequestParam Long screenId,
	        @RequestParam Double basePrice) {

	    showSeatService.generateSeatsForShow(showId, screenId, basePrice);
	    return ResponseEntity.ok("Show seats generated successfully");
	}

	// Get all seats for a show
	@GetMapping
	public ResponseEntity<List<ShowSeatResponseDTO>> getSeats(@PathVariable Long showId) {

		List<ShowSeatResponseDTO> response = showSeatService.getSeatsByShowId(showId).stream()
				.map(seat -> new ShowSeatResponseDTO(seat.getShowId(), seat.getSeatId(), seat.getPrice(),
						seat.getSeatStatus()))
				.toList();

		return ResponseEntity.ok(response);
	}

	// Get available seats
	@GetMapping("/available")
	public ResponseEntity<List<ShowSeatResponseDTO>> getAvailableSeats(@PathVariable Long showId) {

		List<ShowSeatResponseDTO> response = showSeatService.getAvailableSeats(showId).stream()
				.map(seat -> new ShowSeatResponseDTO(seat.getShowId(), seat.getSeatId(), seat.getPrice(),
						seat.getSeatStatus()))
				.toList();

		return ResponseEntity.ok(response);
	}

	/*
	 * Keep Them (Good for testing & admin APIs)
	 * 
	 * Pros:
	 * 
	 * You can test locking manually via Postman
	 * 
	 * Frontend can show seat locking separately
	 * 
	 * Useful for debugging
	 */
	// Lock seats
	@PostMapping("/lock")
	public ResponseEntity<String> lockSeats(@PathVariable Long showId, @RequestBody List<Long> seatIds) {

		showSeatService.lockSeats(showId, seatIds);
		return ResponseEntity.ok("Seats locked successfully");
	}

	// Release seats manually
	@PostMapping("/release")
	public ResponseEntity<String> releaseSeats(@PathVariable Long showId, @RequestBody List<Long> seatIds) {

		showSeatService.releaseSeats(showId, seatIds);
		return ResponseEntity.ok("Seats released successfully");
	}
}
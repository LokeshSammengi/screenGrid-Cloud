package com.cinebook.bookingMS.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinebook.bookingMS.entities.Seat;
import com.cinebook.bookingMS.entities.ShowSeat;
import com.cinebook.bookingMS.enums.SeatStatus;
import com.cinebook.bookingMS.exceptions.ResourceNotFoundException;
import com.cinebook.bookingMS.exceptions.SeatLockedException;
import com.cinebook.bookingMS.repo.SeatRepository;
import com.cinebook.bookingMS.repo.ShowSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowSeatImp implements ShowSeatService {

	private final SeatRepository seatRepo;
	private final ShowSeatRepository showSeatRepo;

	@Override
	@Transactional
	public void generateSeatsForShow(Long showId, Long screenId, Double basePrice) {
		// check whether showid is present or not(feign)
		if (showSeatRepo.existsByShowId(showId)) {
		    throw new IllegalStateException("Show seats already generated");
		}
		List<Seat> seats = seatRepo.findByScreenId(screenId);	

		if (seats.isEmpty()) {
			throw new ResourceNotFoundException("seats are empty for this screenId " + screenId);
		}

		List<ShowSeat> listShowSeats = new ArrayList<>();
		seats.forEach(seat -> {
			ShowSeat showSeat = new ShowSeat();
			showSeat.setShowId(showId);
			showSeat.setSeatId(seat.getId());
			showSeat.setSeatStatus(SeatStatus.AVAILABLE);
			showSeat.setPrice(basePrice);

			listShowSeats.add(showSeat);

		});

		showSeatRepo.saveAll(listShowSeats);
	}

	public List<ShowSeat> getAvailableSeats(Long showId) {
		return showSeatRepo.findByShowIdAndSeatStatus(
				showId,
				SeatStatus.AVAILABLE
		);
	}
/*
	@Transactional
	public void lockSeats(Long showId, List<Long> seatIds) {

		List<ShowSeat> showSeats = showSeatRepo.findByShowIdAndSeatIdIn(showId, seatIds);

		if (showSeats.size() != seatIds.size()) {
			throw new ResourceNotFoundException("One or more seats not found");
		}

		for (ShowSeat showSeat : showSeats) {

			if (showSeat.getSeatStatus() == SeatStatus.BOOKED) {
				throw new IllegalStateException("Seat already booked: " + showSeat.getSeatId());
			}

			if (showSeat.getSeatStatus() == SeatStatus.LOCKED) {

				if (showSeat.getLockedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {

					// expired → reclaim immediately
					showSeat.setSeatStatus(SeatStatus.AVAILABLE);
					showSeat.setLockedAt(null);

				} else {
					throw new IllegalStateException("Seat currently locked: " + showSeat.getSeatId());
				}
			}

			// now safe to lock
			showSeat.setSeatStatus(SeatStatus.LOCKED);
			showSeat.setLockedAt(LocalDateTime.now());
		}

		showSeatRepo.saveAll(showSeats);

	}*/

	@Transactional
	public void lockSeats(Long showId, List<Long> seatIds) {

	    List<ShowSeat> showSeats =
	            showSeatRepo.findByShowIdAndSeatIdInForUpdate(showId, seatIds);

	    if (showSeats.size() != seatIds.size()) {
	        throw new ResourceNotFoundException("One or more seats not found");
	    }

	    for (ShowSeat showSeat : showSeats) {

	        if (showSeat.getSeatStatus() != SeatStatus.AVAILABLE) {
	            throw new IllegalStateException(
	                "Seat not available: " + showSeat.getSeatId()
	            );
	        }

	        showSeat.setSeatStatus(SeatStatus.LOCKED);
	        showSeat.setLockedAt(LocalDateTime.now());
	    }
	}
	@Transactional
	public void confirmSeats(Long showId, List<Long> seatIds) {

		List<ShowSeat> showSeats = showSeatRepo.findByShowIdAndSeatIdIn(showId, seatIds);

		if (seatIds.size() != showSeats.size()) {
			throw new ResourceNotFoundException("One or more seats not found");
		}

		// validate all seats first
		for (ShowSeat showSeat : showSeats) {
			if (showSeat.getSeatStatus() != SeatStatus.LOCKED) {
				throw new SeatLockedException("Seat " + showSeat.getSeatId() + " is not locked");
			}
		}

		// update the status
		showSeats.forEach(showseat -> {
			showseat.setSeatStatus(SeatStatus.BOOKED);
			showseat.setLockedAt(null);
		});

		// save all seats
		showSeatRepo.saveAll(showSeats);
	}

	@Transactional
	public void releaseSeats(Long showId, List<Long> seatIds) {

		List<ShowSeat> showSeats = showSeatRepo.findByShowIdAndSeatIdIn(showId, seatIds);

		if (seatIds.size() != showSeats.size()) {
			throw new ResourceNotFoundException("One or more seats not found");

		}

		for (ShowSeat showSeat : showSeats) {
			if (showSeat.getSeatStatus() == SeatStatus.BOOKED) {
				throw new IllegalStateException("Cannot release already booked seat: " + showSeat.getSeatId());
			}

			if (showSeat.getSeatStatus() != SeatStatus.LOCKED) {
				throw new IllegalStateException("Seat is not in locked state: " + showSeat.getSeatId());
			}
		}

		showSeats.forEach(showseat -> {
			showseat.setSeatStatus(SeatStatus.AVAILABLE);
			showseat.setLockedAt(null);
		});

		showSeatRepo.saveAll(showSeats);
	}
	
	 public List<ShowSeat> getSeatsByShowId(Long showId){
		 List<ShowSeat> showSeats=showSeatRepo.findByShowId(showId);
		 return showSeats;
	 }
}

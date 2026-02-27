package com.cinebook.bookingMS.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinebook.bookingMS.VO.SingleSeatRequest;
import com.cinebook.bookingMS.entities.Seat;
import com.cinebook.bookingMS.exceptions.DuplicateResourceException;
import com.cinebook.bookingMS.exceptions.ResourceNotFoundException;
import com.cinebook.bookingMS.feign.CatalogClient;
import com.cinebook.bookingMS.repo.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatServiceImp implements SeatService {

	private final CatalogClient catalogClient;
	private final SeatRepository seatRepo;

	@Override
	@Transactional
	public Seat createSeat(SingleSeatRequest request) {

		// Validate screen from Catalog Service
		catalogClient.fetchScreenById(request.getScreenId());

		// Check duplicate
		if (seatRepo.existsByScreenIdAndSeatNumber(request.getScreenId(), request.getSeatNumber())) {

			throw new DuplicateResourceException("Seat already exists for this screen");
		}

		Seat seat = new Seat();
		BeanUtils.copyProperties(request, seat);

		return seatRepo.save(seat);
	}

	@Override
	@Transactional
	public List<Seat> createBulkSeat(List<SingleSeatRequest> bulkRequest) {

		if (bulkRequest.isEmpty()) {
			throw new IllegalArgumentException("Seat list cannot be empty");
		}

		Long screenId = bulkRequest.get(0).getScreenId();

		catalogClient.fetchScreenById(screenId);

		Set<String> seatNumbers = new HashSet<>();

		List<Seat> seats = bulkRequest.stream().map(request -> {

			if (!seatNumbers.add(request.getSeatNumber())) {
				throw new DuplicateResourceException("Duplicate seat in request: " + request.getSeatNumber());
			}

			if (seatRepo.existsByScreenIdAndSeatNumber(request.getScreenId(), request.getSeatNumber())) {

				throw new DuplicateResourceException("Seat already exists in DB: " + request.getSeatNumber());
			}

			Seat seat = new Seat();
			BeanUtils.copyProperties(request, seat);
			return seat;

		}).toList();

		return seatRepo.saveAll(seats);
	}

	@Override
	public Seat getSeatById(Long id) {
		Seat seat = seatRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
		return seat;
	}

	@Override
	public List<Seat> getSeatsByScreenId(Long screenId) {

		catalogClient.fetchScreenById(screenId);

		List<Seat> seats = seatRepo.findByScreenId(screenId);

		if (seats.isEmpty()) {
			throw new ResourceNotFoundException("No seats found for this screen");
		}

		return seats;
	}

}

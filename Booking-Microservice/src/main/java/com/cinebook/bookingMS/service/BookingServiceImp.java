package com.cinebook.bookingMS.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinebook.bookingMS.VO.BookingResponse;
import com.cinebook.bookingMS.VO.PaymentRequest;
import com.cinebook.bookingMS.VO.PaymentResponse;
import com.cinebook.bookingMS.VO.ShowSeatResponseDTO;
import com.cinebook.bookingMS.entities.Booking;
import com.cinebook.bookingMS.entities.ShowSeat;
import com.cinebook.bookingMS.enums.BookingStatus;
import com.cinebook.bookingMS.enums.SeatStatus;
import com.cinebook.bookingMS.exceptions.ResourceNotFoundException;
import com.cinebook.bookingMS.feign.PaymentClient;
import com.cinebook.bookingMS.repo.BookingRepository;
import com.cinebook.bookingMS.repo.ShowSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

	private final BookingRepository bookingRepo;
	private final ShowSeatService showSeatService;
	private final ShowSeatRepository showSeatRepo;
	private final PaymentClient paymentClient;

	@Transactional
	public Double calculateTotal(Long showId, List<Long> seatIds) {

		Double totalAmount = 0.0;
		for (Long seatId : seatIds) {
			ShowSeat showSeat = showSeatRepo.findByShowIdAndSeatId(showId, seatId)
					.orElseThrow(() -> new ResourceNotFoundException("Seat not found for this show"));

			totalAmount += showSeat.getPrice();
		}
		return totalAmount;
	}

//	@Transactional
	private Booking saveBooking(Long userId, Long showId, List<Long> seatIds) {
		// check user id also using feign client whether it is available or not (userid
		// is customer ornot if not throw exception

		// 1*Validate Seats exist
		if (seatIds == null || seatIds.isEmpty()) {
			throw new ResourceNotFoundException("Seat list cannot be empty");
		}

		// validate seats (AVAILABLE)
		for (Long seatId : seatIds) {

			ShowSeat showSeat = showSeatRepo.findByShowIdAndSeatId(showId, seatId)
					.orElseThrow(() -> new ResourceNotFoundException("Seat not found for this show"));

			if (showSeat.getSeatStatus() == SeatStatus.BOOKED)
				throw new IllegalStateException("Seat already booked");
		}

		// 2 lock seats
		showSeatService.lockSeats(showId, seatIds);

		List<ShowSeat> bookedSeats = new ArrayList<>();

		for (Long seatId : seatIds) {
			ShowSeat showSeat = showSeatRepo.findByShowIdAndSeatId(showId, seatId)
					.orElseThrow(() -> new ResourceNotFoundException("Seat not found for this show"));
			bookedSeats.add(showSeat);
		}

		// 3.create booking
		Booking booking = new Booking();
		booking.setShowId(showId);
		booking.setUserId(userId);
		booking.setTotalAmount(calculateTotal(showId, seatIds));
		booking.setBookingStatus(BookingStatus.PENDING);

		booking.setBookingTime(LocalDateTime.now());
		booking.setExpiryTime(LocalDateTime.now().plusMinutes(10));
		Booking savedBooking = bookingRepo.save(booking);

		for (ShowSeat showSeat : bookedSeats) {
			showSeat.setBooking(savedBooking);
			showSeatRepo.save(showSeat);
		}
		booking.setShowSeats(bookedSeats);

		return savedBooking;
	}

	private PaymentResponse createPaymentWithCB(PaymentRequest request) {
		PaymentResponse response=paymentClient.createPayment(request);
		return response;
	}
//	I think this is main important method (heart of booking module business service)
//	@Transactional
	public String createBooking(Long userId, Long showId, List<Long> seatIds) {

		Booking savedBooking = saveBooking(userId, showId, seatIds);

		// call payment service
		PaymentRequest request = new PaymentRequest();
		request.setBookingId(savedBooking.getId());
		request.setAmount(savedBooking.getTotalAmount());
		request.setPaymentMethod("CARD");

		PaymentResponse response = createPaymentWithCB(request);

		// save booking
		return response.getCheckoutUrl();
	}

	

	// This method should be INTERNAL (used by Payment success).
	@Transactional
	public void confirmBooking(Long bookingId) {

		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
			throw new RuntimeException("Seats are booked --idempotent behavior dont call confirm booking twice...."); // idempotent
																														// behavior
		}

		if (booking.getBookingStatus() != BookingStatus.PENDING) {
			throw new IllegalStateException("Invalid booking state seat booked by someone or not available");
		}

		if (booking.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Booking expired");
		}

		for (ShowSeat showSeat : booking.getShowSeats()) {

			if (showSeat.getSeatStatus() != SeatStatus.LOCKED) {
				throw new IllegalStateException("Seat not locked");
			}

			showSeat.setSeatStatus(SeatStatus.BOOKED);
			showSeat.setLockedAt(null);
			showSeatRepo.save(showSeat);
		}

		booking.setBookingStatus(BookingStatus.CONFIRMED);

		bookingRepo.save(booking);
	}

	@Transactional
	public void cancelBooking(Long bookingId) {

		/*
		 * This is required for: Payment failure Lock expiry Manual cancel
		 */
		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (booking.getBookingStatus() != BookingStatus.PENDING) {
			throw new IllegalStateException("Cannot cancel processed booking");
		}

		for (ShowSeat showSeat : booking.getShowSeats()) {
			showSeat.setSeatStatus(SeatStatus.AVAILABLE);
			showSeat.setLockedAt(null);
			showSeatRepo.save(showSeat);
		}

		booking.setBookingStatus(BookingStatus.CANCELLED);

		bookingRepo.save(booking);
	}

	// get bookings by id
	@Transactional
	public BookingResponse getBookingById(Long bookingId) {
		
		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with Id : "+bookingId));
	
		BookingResponse response = new BookingResponse();
		response.setUserId(booking.getUserId());
		response.setShowId(booking.getShowId());
		response.setTotalAmount(booking.getTotalAmount());
		response.setBookingStatus(booking.getBookingStatus());
		List<ShowSeat> listShowSeat = new ArrayList<ShowSeat>();
//		List<ShowSeatResponseDTO> listShowSeatResponseDto = new ArrayList<ShowSeatResponseDTO>();
//		listShowSeat.forEach(showSeat->{
//			ShowSeatResponseDTO ssResponseDto = new ShowSeatResponseDTO();
//			ssResponseDto.setShowId(showSeat.getShowId());
//			ssResponseDto.setSeatId(showSeat.getSeatId());
//			ssResponseDto.setPrice(showSeat.getPrice());
//			ssResponseDto.setSeatStatus(showSeat.getSeatStatus());
//			listShowSeatResponseDto.add(ssResponseDto);
//		});
//		response.setSeats(listShowSeatResponseDto);
		response.setSeats(listShowSeat.stream().map(
				ss->{
					ShowSeatResponseDTO ssResponseDto = new ShowSeatResponseDTO();
					ssResponseDto.setShowId(ss.getShowId());
					ssResponseDto.setSeatId(ss.getSeatId());
					ssResponseDto.setPrice(ss.getPrice());
					ssResponseDto.setSeatStatus(ss.getSeatStatus());
					return ssResponseDto;
				}).collect(Collectors.toList())
				);
		return response;
		/*List<ShowSeatResponseDTO> listShowSeatResponse = new ArrayList<ShowSeatResponseDTO>();
		booking.getShowSeats().forEach(seat -> {
			ShowSeatResponseDTO responseDto = new ShowSeatResponseDTO();
			
			responseDto.setShowId(seat.getShowId());
			responseDto.setSeatId(seat.getSeatId());
			responseDto.setPrice(seat.getPrice());
			responseDto.setSeatStatus(seat.getSeatStatus());
			
			listShowSeatResponse.add(responseDto);
		});
		// convert booking entity into response
		BookingResponse response = new BookingResponse();
		BeanUtils.copyProperties(booking, response);
		response.setSeats(listShowSeatResponse);
*/
//		return response;
	}

//	// get all bookings
//	@Transactional
//	public List<Booking> getAllBookings() {
//		List<Booking> bookings = bookingRepo.findAllWithSeats();
//		return bookings;
//	}

	// remove booking details
	@Transactional
	public void removeBookings(Long bookingId) {
		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		bookingRepo.delete(booking);
	}

	@Transactional
	public void updateBookingStatus(Long bookingId, BookingStatus status) {

		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (booking.getBookingStatus() != BookingStatus.PENDING) {
			throw new IllegalStateException("Booking already processed");
		}

		booking.setBookingStatus(status);
		bookingRepo.save(booking);
	}

	// Payment service should call this before initiating payment
	@Transactional
	public Booking validateBookingForPayment(Long bookingId) {

		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (booking.getBookingStatus() != BookingStatus.PENDING) {
			throw new IllegalStateException("Booking not available for payment");
		}

		if (booking.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Booking expired");
		}

		return booking;
	}

//	Payment service should call this before redirecting to payment gateway.
	@Transactional
	public Booking markPaymentStarted(Long bookingId) {

		Booking booking = bookingRepo.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		if (booking.getBookingStatus() != BookingStatus.PENDING) {
			throw new IllegalStateException("Invalid booking state");
		}

		booking.setBookingStatus(BookingStatus.PAYMENT_IN_PROGRESS);
		return bookingRepo.save(booking);
	}

	// payment service calls this when payment fails
	@Transactional
	public void handlePaymentFailure(Long bookingId) {
		cancelBooking(bookingId);
	}

	public PaymentResponse paymentFallback(PaymentRequest request, Throwable ex) {

		System.out.println("⚠ Payment Service is down. Circuit breaker activated.");
		System.out.println("Reason: " + ex.getMessage());

		// Option 1: Mark booking as PAYMENT_PENDING
		Booking booking = bookingRepo.findById(request.getBookingId())
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		booking.setBookingStatus(BookingStatus.PENDING);
		bookingRepo.save(booking);

		// Option 2: Return a safe response
		PaymentResponse fallbackResponse = new PaymentResponse();
		fallbackResponse.setCheckoutUrl(null);

		return fallbackResponse;
	}

	@Override
	public List<Booking> getAllBookings() {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.cinebook.payment.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinebook.payment.VO.BookingResponse;
import com.cinebook.payment.VO.PaymentRequest;
import com.cinebook.payment.VO.PaymentResponse;
import com.cinebook.payment.VO.StripeSessionResponse;
import com.cinebook.payment.entity.Payment;
import com.cinebook.payment.enums.PaymentStatus;
import com.cinebook.payment.feign.BookingClient;
import com.cinebook.payment.repo.PaymentRepository;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final StripeSessionImp stripeSessionImp;
	private final BookingClient bookingClient;

	/*
	 * @Transactional public PaymentResponse createPayment(PaymentRequest
	 * paymentRequest) {
	 * 
	 * // call booking service using feign client // 1. booking exists(unnecessary)
	 * BookingResponse booking =
	 * bookingClient.getBookingsById(paymentRequest.getBookingId()); // 2. booking
	 * status is PENDING
	 * 
	 * if (!booking.getBookingStatus().equals(BookingStatus.PENDING)) { throw new
	 * RuntimeException("Booking is not in PENDING state"); }
	 * 
	 * if (!booking.getTotalAmount().equals(paymentRequest.getAmount())) { throw new
	 * IllegalArgumentException("Amount is not equalized...."); }
	 * 
	 * // check whether bookingid is their or not if possible Payment payment = new
	 * Payment();
	 * 
	 * // 1.1 create payment ->status INITIATED
	 * payment.setTransactionId(UUID.randomUUID().toString());
	 * payment.setBookingId(paymentRequest.getBookingId());
	 * payment.setAmount(paymentRequest.getAmount());
	 * payment.setStatus(PaymentStatus.INITIATED);
	 * 
	 * // 1.2 Call payment provider (STRIPE/Razoro pay method) Boolean paymentstatus
	 * = callPaymentProvider(payment);
	 * 
	 * payment.setPaymentMethod("CARD"); payment.setProvider("STRIPE");
	 * payment.setCurrency("INR");
	 * 
	 * // 1.3 update status -> SUCCESS / FAILED if (paymentstatus) {
	 * 
	 * payment.setStatus(PaymentStatus.SUCCESS); payment.setProviderPaymentId("AxTy"
	 * + UUID.randomUUID()); payment.setPaymentTime(LocalDateTime.now());
	 * 
	 * bookingClient.confirmBooking(paymentRequest.getBookingId());
	 * 
	 * } else {
	 * 
	 * payment.setStatus(PaymentStatus.FAILED); payment.setProviderPaymentId(null);
	 * payment.setFailureReason("Payment declined");
	 * 
	 * bookingClient.cancelBooking(paymentRequest.getBookingId()); } // SAVE
	 * Initiated PAYMENT paymentRepository.save(payment);
	 * 
	 * 
	 * 
	 * return response; }
	 */

//	private boolean callPaymentProvider(Payment payment) {
//	// simulate success or failure
//	return  true;
//}

	@Transactional
	public PaymentResponse createPayment(PaymentRequest paymentRequest) throws StripeException {
		// request -> bookingId, Amount, payment method(card)

		// check whether booking id existed or not
		BookingResponse bookingResposne = bookingClient.getBookingById(paymentRequest.getBookingId());

		Payment payment = new Payment();
		payment.setTransactionId(UUID.randomUUID().toString());
		payment.setBookingId(paymentRequest.getBookingId());
		payment.setAmount(paymentRequest.getAmount());
		payment.setStatus(PaymentStatus.INITIATED);
		payment.setPaymentMethod("CARD");
		payment.setProvider("STRIPE");
		payment.setCurrency("INR");

		paymentRepository.save(payment);

		try {
			StripeSessionResponse sessionResponse = stripeSessionImp.createPaymentSession(paymentRequest.getBookingId(),
					paymentRequest.getAmount());
			payment.setProviderPaymentId(sessionResponse.getSessionId());
			paymentRepository.save(payment);

			PaymentResponse paymentResponse = new PaymentResponse();
			paymentResponse.setCheckoutUrl(sessionResponse.getCheckoutUrl());
			paymentResponse.setTransactionId(payment.getTransactionId());

//		    5. Return Checkout URL
			return paymentResponse;
		} catch (Exception e) {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setFailureReason("stripe session creation failed");
			paymentRepository.save(payment);
			throw e;
		}

	}

	/*
	 * @Transactional public void handlePaymentSuccess(Long bookingId) {
	 * 
	 * Payment payment = paymentRepository .findByBookingId(bookingId)
	 * .orElseThrow();
	 * 
	 * // prevent duplicate processing if (payment.getStatus() ==
	 * PaymentStatus.SUCCESS) { return; }
	 * 
	 * payment.setStatus(PaymentStatus.SUCCESS);
	 * payment.setPaymentTime(LocalDateTime.now());
	 * 
	 * paymentRepository.save(payment);
	 * 
	 * // confirm booking bookingClient.confirmBooking(bookingId); }
	 */

}

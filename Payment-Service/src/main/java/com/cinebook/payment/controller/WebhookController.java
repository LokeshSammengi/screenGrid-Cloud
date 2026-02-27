package com.cinebook.payment.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinebook.payment.entity.Payment;
import com.cinebook.payment.enums.PaymentStatus;
import com.cinebook.payment.exceptions.ResourceNotFoundException;
import com.cinebook.payment.feign.BookingClient;
import com.cinebook.payment.repo.PaymentRepository;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/paymentWebhook")
@RequiredArgsConstructor
public class WebhookController {

	private final BookingClient bookingClient;
	private final PaymentRepository paymentRepo;

	@CircuitBreaker(name = "bookingService", fallbackMethod = "bookingFallback")
	public void confirmBookingWithCB(Long bookingId) {
		bookingClient.confirmBooking(bookingId);
	}

//	private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

	// Replace with your endpoint secret from the Stripe Dashboard stripe cli gives one code replace here that unique 
	private static final String ENDPOINT_SECRET ="whsec_*********";
	
	@PostMapping("/webhook")
	@CircuitBreaker(name = "bookingService", fallbackMethod = "bookingFallback")
	public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) throws Exception {

		String payload = new String(request.getInputStream().readAllBytes());
		String sigHeader = request.getHeader("Stripe-Signature");
		System.out.println("sigheder : " + sigHeader);
		Event event = Webhook.constructEvent(payload, sigHeader, ENDPOINT_SECRET);

		if ("checkout.session.completed".equals(event.getType())) {
			System.out.println("Event Type: " + event.getType());
			System.out.println("Event ID: " + event.getId());
			System.out.println("Raw Object: " + event.getDataObjectDeserializer().getObject());

			EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

			StripeObject stripeObject = null;

			if (deserializer.getObject().isPresent()) {
				stripeObject = deserializer.getObject().get();
			} else {
				stripeObject = deserializer.deserializeUnsafe();
			}

			Session session = (Session) stripeObject;
//			String transactionId = session.getId();
			System.out.println("Session ID from webhook: " + session.getId());
//			Payment payment = paymentRepo.findByTransactionId(transactionId)
//					.orElseThrow(() -> new RuntimeException("Payment not found"));

			Payment payment = paymentRepo.findByProviderPaymentId(session.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
			
			if (payment.getStatus() == PaymentStatus.SUCCESS) {
			    return ResponseEntity.ok("Already processed");
			}

			payment.setStatus(PaymentStatus.SUCCESS);
			payment.setPaymentTime(LocalDateTime.now());
			payment.setUpdatedAt(LocalDateTime.now());
			paymentRepo.save(payment);

			confirmBookingWithCB(payment.getBookingId()); // circuit breaker
		}
//		else { once confirm booking is done then we can perform cancel booking
//			bookingClient.cancelBooking(request.get)
//		}

		return ResponseEntity.ok("Webhook handled");
	}

	/*
	 * public void bookingFallback(Long bookingId, Throwable ex) {
	 * 
	 * System.out.println("⚠ Booking service is down. Circuit breaker activated.");
	 * System.out.println("Reason: " + ex.getMessage());
	 * 
	 * // Instead of failing webhook, // mark payment as SUCCESS but booking
	 * confirmation pending
	 * 
	 * Payment payment = paymentRepo.findByBookingId(bookingId) .orElseThrow(() ->
	 * new ResourceNotFoundException("Payment not found for booking"));
	 * 
	 * payment.setStatus(PaymentStatus.SUCCESS); paymentRepo.save(payment);
	 * 
	 * }
	 */

}

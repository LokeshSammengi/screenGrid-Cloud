package com.cinebook.payment.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinebook.payment.VO.PaymentRequest;
import com.cinebook.payment.VO.PaymentResponse;
import com.cinebook.payment.service.PaymentService;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/generateBill")
	public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) throws StripeException {
		PaymentResponse response = paymentService.createPayment(request);
		return new ResponseEntity<PaymentResponse>(response, HttpStatus.CREATED);
	}

	@GetMapping("/success")
	public ResponseEntity<String> success(@RequestParam("session_id") String sessionId) {
//	    return ResponseEntity.ok("Payment successful. You can close this page.");
		return ResponseEntity.ok("redirect:/success.html");
	}

	@GetMapping("/cancel")
	public ResponseEntity<String> cancel() {
	    return ResponseEntity.ok("Payment cancelled.");
	}

}

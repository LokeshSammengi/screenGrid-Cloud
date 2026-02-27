package com.cinebook.payment.service;

import com.stripe.exception.StripeException;

public interface StripeSession {

	public String createPaymentSession(Long bookingId, Double amount) throws StripeException;
	
}

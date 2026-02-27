package com.cinebook.payment.service;

import com.cinebook.payment.VO.PaymentRequest;
import com.cinebook.payment.VO.PaymentResponse;
import com.cinebook.payment.entity.Payment;
import com.stripe.exception.StripeException;

public interface PaymentService {

	public PaymentResponse createPayment(PaymentRequest paymentRequest) throws StripeException;

//	public void handlePaymentSuccess(Long bookingId);
}

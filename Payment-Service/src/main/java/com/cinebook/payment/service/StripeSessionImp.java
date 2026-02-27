package com.cinebook.payment.service;

import org.springframework.stereotype.Component;

import com.cinebook.payment.VO.StripeSessionResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StripeSessionImp  {
	
	public StripeSessionResponse createPaymentSession(Long bookingId, Double amount) throws StripeException {
		System.out.println("BookingId from metadata: " + bookingId);
	    SessionCreateParams params =
	            SessionCreateParams.builder()
	                    .setMode(SessionCreateParams.Mode.PAYMENT)
	                    .setSuccessUrl("http://localhost:7063/payment/success?session_id={CHECKOUT_SESSION_ID}")
	                    .setCancelUrl("http://localhost:7063/payment/cancel")
	                    .addLineItem(
	                            SessionCreateParams.LineItem.builder()
	                                    .setQuantity(1L)
	                                    .setPriceData(
	                                            SessionCreateParams.LineItem.PriceData.builder()
	                                                    .setCurrency("inr")
	                                                    .setUnitAmount((long) (amount * 100)) // paise
	                                                    .setProductData(
	                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                                                                    .setName("Movie Ticket Booking")
	                                                                    .build()
	                                                    )
	                                                    .build()
	                                    )
	                                    .build()
	                    )
	                    .putMetadata("bookingId", bookingId.toString())
	                    .build();
	  	   
	    Session session = Session.create(params);
	   
	    StripeSessionResponse response = new StripeSessionResponse();
	    response.setSessionId(session.getId());
	    response.setCheckoutUrl(session.getUrl());
	    
	    return response;
	 
	}
		
}

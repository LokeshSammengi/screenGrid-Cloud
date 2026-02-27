package com.cinebook.payment.enums;

public enum PaymentStatus {
	
	INITIATED,//created
    SUCCESS,
    FAILED, //if balance is insufficent
    CANCELLED,//if user implicity wants to cancel the payment 
    REFUNDED

}

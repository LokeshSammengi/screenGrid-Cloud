package com.cinebook.bookingMS.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cinebook.bookingMS.VO.PaymentRequest;
import com.cinebook.bookingMS.VO.PaymentResponse;

@FeignClient(name = "Payment-Service")
public interface PaymentClient {
	
	@PostMapping("/payment/generateBill")
	PaymentResponse createPayment(@RequestBody PaymentRequest request);


}

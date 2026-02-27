package com.cinebook.payment.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String checkoutUrl;
    private String transactionId;
}

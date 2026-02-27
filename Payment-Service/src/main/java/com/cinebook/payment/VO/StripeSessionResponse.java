package com.cinebook.payment.VO;

import lombok.Data;

@Data
public class StripeSessionResponse {
    private String sessionId;
    private String checkoutUrl;
}

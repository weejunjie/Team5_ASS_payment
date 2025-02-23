package com.nusiss.ass.payment.service;

import com.nusiss.ass.payment.dto.PaymentGatewayResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentGatewayService {


    public PaymentGatewayResponseDto processPaymentSuccess(java.math.BigDecimal amount, String accessToken) {
        PaymentGatewayResponseDto response = new PaymentGatewayResponseDto();
        response.setSuccess(true);
        // Generate a fake transaction id
        response.setTransactionId("TXN-" + UUID.randomUUID().toString());
        response.setAmount(amount);
        return response;
    }

    public PaymentGatewayResponseDto processPaymentFailed(java.math.BigDecimal amount, String accessToken) {
        PaymentGatewayResponseDto response = new PaymentGatewayResponseDto();
        response.setSuccess(false);
        response.setAmount(amount);
        return response;
    }
}

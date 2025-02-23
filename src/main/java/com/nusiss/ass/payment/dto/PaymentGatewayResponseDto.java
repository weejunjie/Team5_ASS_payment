package com.nusiss.ass.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentGatewayResponseDto {
    private boolean success;
    private String transactionId;
    private BigDecimal amount;
}

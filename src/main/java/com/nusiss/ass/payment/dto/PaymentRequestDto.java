package com.nusiss.ass.payment.dto;

import com.nusiss.ass.payment.model.Payment.PaymentMethod;
import lombok.Data;


@Data
public class PaymentRequestDto {
    private String bookingId;
    private PaymentMethod paymentMethod;
    private String paymentStatus;
}

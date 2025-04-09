package com.nusiss.ass.payment.controller;

import com.nusiss.ass.payment.dto.PaymentRequestDto;
import com.nusiss.ass.payment.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.nusiss.ass.payment.service.PaymentService;
import com.nusiss.ass.payment.model.Payment;

import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public PaymentResponseDto createPayment(@RequestBody PaymentRequestDto request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable String bookingId) {
        Payment payment = paymentService.getPaymentById(bookingId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        PaymentResponseDto response = new PaymentResponseDto();
        response.setPaymentId(payment.getPaymentId());
        response.setBookingId(bookingId);
        response.setAmount(payment.getAmount());
        response.setTransactionId(payment.getTransactionId());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus().toString());
        response.setCreatedDate(payment.getCreatedDate());
        return ResponseEntity.ok(response);
    }

}

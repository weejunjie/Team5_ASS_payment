package com.nusiss.ass.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import com.nusiss.ass.payment.dto.PaymentGatewayResponseDto;
import com.nusiss.ass.payment.dto.PaymentRequestDto;
import com.nusiss.ass.payment.dto.PaymentResponseDto;
import com.nusiss.ass.payment.model.Booking;
import com.nusiss.ass.payment.model.Payment;
import com.nusiss.ass.payment.model.Payment.Status;
import com.nusiss.ass.payment.dao.BookingRepository;
import com.nusiss.ass.payment.dao.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto request) {
        PaymentResponseDto response = new PaymentResponseDto();

        // Validate booking exists
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Create new Payment entity
        Payment payment = new Payment();
        String paymentId = "PAY-" + UUID.randomUUID();
        LocalDateTime createdTime = LocalDateTime.now();
        // Generate a unique paymentId (for example, using UUID)
        payment.setPaymentId(paymentId);
        payment.setBookingId(booking);
        payment.setAmount(booking.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(Status.PENDING);
        payment.setCreatedDate(createdTime);
        payment.setUpdatedDate(createdTime);

        // Save the new payment record
        payment = paymentRepository.save(payment);

        payment.setStatus(Status.PENDING);

        if(!request.getPaymentMethod().toString().toUpperCase().contains("CASH")){
            PaymentGatewayResponseDto gatewayResponse = null;
            // Call the mock payment gateway (bank or card service)
            if(request.getPaymentStatus().equalsIgnoreCase("Success")){
                gatewayResponse = paymentGatewayService.processPaymentSuccess(booking.getAmount(), "abc123");
            } else {
                gatewayResponse = paymentGatewayService.processPaymentFailed(booking.getAmount(), "abc123");
            }

            // Update the payment record based on gateway response
            if (gatewayResponse.isSuccess()) {
                payment.setStatus(Status.SUCCESS);
            } else {
                payment.setStatus(Status.FAILED);
            }
            payment.setTransactionId(gatewayResponse.getTransactionId());
            payment.setUpdatedDate(LocalDateTime.now());
            paymentRepository.save(payment);
        }

        // Prepare the response
        response.setPaymentId(paymentId);
        response.setBookingId(request.getBookingId());
        response.setAmount(booking.getAmount());
        response.setTransactionId(payment.getTransactionId());
        response.setPaymentMethod(request.getPaymentMethod());
        response.setStatus(payment.getStatus().toString());
        response.setCreatedDate(createdTime);
        return response;
    }

    public Payment getPaymentById(String paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        return optionalPayment.orElse(null);
    }
}

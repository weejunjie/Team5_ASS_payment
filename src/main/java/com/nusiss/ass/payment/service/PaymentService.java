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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    private static final String SECRET_KEY = "gVuoc5zK4F9Ukr8aWNkohE5ppUZOy2XEjPIGswL0EZo=";

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto request) {
        PaymentResponseDto response = new PaymentResponseDto();
        try {
            // Validate booking exists
            Booking booking = bookingRepository.findById(encrypt(request.getBookingId()))
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            Payment payment = new Payment();
            String paymentId = "PAY-" + UUID.randomUUID();
            LocalDateTime createdTime = LocalDateTime.now();

            payment.setPaymentId(encrypt(paymentId));
            payment.setBookingId(booking);
            payment.setAmount(booking.getAmount());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setStatus(Status.PENDING);
            payment.setCreatedDate(createdTime);
            payment.setUpdatedDate(createdTime);

            // Save the new payment record
            payment = paymentRepository.save(payment);

            payment.setStatus(Status.PENDING);

            if (!request.getPaymentMethod().toString().toUpperCase().contains("CASH")) {
                PaymentGatewayResponseDto gatewayResponse = null;
                // Call the mock payment gateway (bank or card service)
                if (request.getPaymentStatus().equalsIgnoreCase("Success")) {
                    gatewayResponse = paymentGatewayService.processPaymentSuccess(booking.getAmount(), "encryptedToken");
                } else {
                    gatewayResponse = paymentGatewayService.processPaymentFailed(booking.getAmount(), "encryptedToken");
                }

                // Update the payment record based on gateway response
                if (gatewayResponse.isSuccess()) {
                    payment.setStatus(Status.SUCCESS);
                } else {
                    payment.setStatus(Status.FAILED);
                }
                payment.setTransactionId(encrypt(gatewayResponse.getTransactionId()));
                payment.setUpdatedDate(LocalDateTime.now());
                paymentRepository.save(payment);
            }

            // Prepare the response
            response.setPaymentId(paymentId);
            response.setBookingId(request.getBookingId());
            response.setAmount(booking.getAmount());
            response.setTransactionId(decrypt(payment.getTransactionId()));
            response.setPaymentMethod(request.getPaymentMethod());
            response.setStatus(payment.getStatus().toString());
            response.setCreatedDate(createdTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Payment getPaymentById(String bookingId) {
        Optional<Payment> optionalPayment = null;
        try {
            optionalPayment = paymentRepository.findByBookingId(encrypt(bookingId));

            if (optionalPayment.isPresent()) {
                Payment payment = optionalPayment.get();
                payment.setPaymentId(decrypt(payment.getPaymentId()));
                payment.setTransactionId(decrypt(payment.getTransactionId()));
                return payment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalPayment.orElse(null);
    }

    public static String encrypt(String data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
}

package com.nusiss.ass.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import com.nusiss.ass.payment.dao.PaymentRepository;
import com.nusiss.ass.payment.model.Payment;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        payment.setTransactionId(UUID.randomUUID().toString());
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPayment(Long id) {
        return paymentRepository.findById(id);
    }
}

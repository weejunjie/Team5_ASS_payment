package com.nusiss.ass.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @GetMapping("/{id}")
    public Optional<Payment> getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

}

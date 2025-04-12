package com.rentify.payment;



import com.nusiss.ass.payment.PaymentServiceApplication;
import com.nusiss.ass.payment.model.Booking;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.nusiss.ass.payment.dto.PaymentRequestDto;
import com.nusiss.ass.payment.dto.PaymentResponseDto;
import com.nusiss.ass.payment.model.Payment;
import com.nusiss.ass.payment.dao.BookingRepository;
import com.nusiss.ass.payment.dao.PaymentRepository;
import com.nusiss.ass.payment.service.PaymentService;
import com.nusiss.ass.payment.controller.PaymentController;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@SpringBootTest(classes = PaymentServiceApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentControllerTest {

    @Autowired
    private PaymentController paymentController;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeAll
    public static void setUp(@Autowired PaymentRepository paymentRepository,
                             @Autowired BookingRepository bookingRepository,
                             @Autowired PaymentService paymentService) {
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        Booking booking = new Booking();
        try{
            booking.setBookingId(paymentService.encrypt("BOOK-1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        booking.setAmount(new BigDecimal("100.10"));

        bookingRepository.save(booking);
    }

    @Test
    public void testCreatePaymentSuccess() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setBookingId("BOOK-1");
        paymentRequestDto.setPaymentMethod(Payment.PaymentMethod.BANK_A);
        paymentRequestDto.setPaymentStatus("Success");

        ResponseEntity<PaymentResponseDto> response = paymentController.createPayment(paymentRequestDto);
        assertEquals(200, response.getStatusCodeValue());

        PaymentResponseDto body = response.getBody();

        assertNotNull(body);

        assertEquals("BOOK-1", body.getBookingId());
        assertEquals(Payment.PaymentMethod.BANK_A, body.getPaymentMethod());
        assertEquals("SUCCESS", body.getStatus());
    }

    @Test
    public void testCreatePaymentFailed() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setBookingId("BOOK-2");
        paymentRequestDto.setPaymentMethod(Payment.PaymentMethod.BANK_A);
        paymentRequestDto.setPaymentStatus("SUCCESS");

        ResponseEntity<PaymentResponseDto> response = paymentController.createPayment(paymentRequestDto);

        assertEquals(404, response.getStatusCodeValue());

        PaymentResponseDto body = response.getBody();

        assertNotNull(body);

        assertEquals(null, body.getBookingId());
        assertEquals(null, body.getPaymentMethod());
        assertEquals(null, body.getStatus());
    }

    @Test
    public void testGetPaymentSuccess() {
        ResponseEntity<PaymentResponseDto> response = paymentController.getPayment("BOOK-1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        PaymentResponseDto body = response.getBody();
        assertNotNull(body);

        assertEquals("BOOK-1", body.getBookingId());
        assertEquals(new BigDecimal("101.10"), body.getAmount());
        assertEquals(Payment.PaymentMethod.BANK_A, body.getPaymentMethod());
        assertEquals("SUCCESS", body.getStatus());
    }

    @Test
    public void testGetPaymentFailed() {
        ResponseEntity<PaymentResponseDto> response = paymentController.getPayment("BOOK-2");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @AfterAll
    public static void tearDownAll(@Autowired PaymentRepository paymentRepository,
                                   @Autowired BookingRepository bookingRepository) {
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
    }

}

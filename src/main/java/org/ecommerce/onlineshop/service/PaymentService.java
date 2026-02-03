package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.domain.Payment;
import org.ecommerce.onlineshop.domain.PaymentStatus;
import org.ecommerce.onlineshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Order order, BigDecimal amount, PaymentStatus status) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByOrder(Order order) {
        return paymentRepository.findByOrder(order);
    }
}


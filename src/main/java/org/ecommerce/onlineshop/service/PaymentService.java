package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.domain.Payment;
import org.ecommerce.onlineshop.domain.PaymentStatus;
import org.ecommerce.onlineshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public BigDecimal getTotalRevenue() {
        BigDecimal completed = paymentRepository.sumAmountByStatus(PaymentStatus.COMPLETED);
        return completed != null ? completed : BigDecimal.ZERO;
    }
    
    public Long getTotalCompletedPayments() {
        return paymentRepository.countByStatus(PaymentStatus.COMPLETED);
    }
    
    public Map<PaymentStatus, Long> getPaymentCountsByStatus() {
        Map<PaymentStatus, Long> counts = new HashMap<>();
        for (PaymentStatus status : PaymentStatus.values()) {
            counts.put(status, paymentRepository.countByStatus(status));
        }
        return counts;
    }
    
    public Map<PaymentStatus, BigDecimal> getPaymentAmountsByStatus() {
        Map<PaymentStatus, BigDecimal> amounts = new HashMap<>();
        for (PaymentStatus status : PaymentStatus.values()) {
            BigDecimal amount = paymentRepository.sumAmountByStatus(status);
            amounts.put(status, amount != null ? amount : BigDecimal.ZERO);
        }
        return amounts;
    }
}


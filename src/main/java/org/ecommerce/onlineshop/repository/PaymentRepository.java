package org.ecommerce.onlineshop.repository;

import org.ecommerce.onlineshop.domain.Payment;
import org.ecommerce.onlineshop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrder(Order order);
}


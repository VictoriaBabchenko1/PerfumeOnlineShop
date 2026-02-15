package org.ecommerce.onlineshop.repository;

import org.ecommerce.onlineshop.domain.Payment;
import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrder(Order order);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") PaymentStatus status);
}


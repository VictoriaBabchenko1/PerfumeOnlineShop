package org.ecommerce.onlineshop.repository;

import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByDateTimeDesc(Long userId);
    List<Order> findAllByOrderByDateTimeDesc();
    
    List<Order> findByStatus(OrderStatus status);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    BigDecimal sumTotalByStatus(@Param("status") OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);
    
    @Query("SELECT AVG(o.total) FROM Order o WHERE o.status = :status")
    BigDecimal averageTotalByStatus(@Param("status") OrderStatus status);
}

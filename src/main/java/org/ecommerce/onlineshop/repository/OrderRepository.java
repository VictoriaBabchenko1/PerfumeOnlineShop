package org.ecommerce.onlineshop.repository;

import org.ecommerce.onlineshop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByDateTimeDesc(Long userId);
    List<Order> findAllByOrderByDateTimeDesc();
}

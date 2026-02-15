package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.domain.OrderItem;
import org.ecommerce.onlineshop.domain.OrderStatus;
import org.ecommerce.onlineshop.exeptions.FieldValidationException;
import org.ecommerce.onlineshop.repository.OrderRepository;
import org.ecommerce.onlineshop.utils.FieldsValidationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final CartService cartService;
    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(Long userId, String firstName, String lastName, String city, String email,
                             String phoneNumber, String postIndex) {
        if(!FieldsValidationUtils.isValidEmail(email)) {
            throw new FieldValidationException("Invalid email format");
        }
        if(!FieldsValidationUtils.doesContainOnlyDigits(phoneNumber)) {
            throw new FieldValidationException("Phone number must contain only digits");
        }
        if (!FieldsValidationUtils.doesContainOnlyDigits(postIndex)) {
            throw new FieldValidationException("Post index must contain only digits");
        }

        List<CartItem> cartItems = cartService.getCartItems(userId);
        BigDecimal total = cartService.calculateTotalPrice(cartItems);

        Order order = new Order();
        order.setUserId(userId);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setCity(city);
        order.setEmail(email);
        order.setPhoneNumber(phoneNumber);
        order.setPostIndex(Integer.parseInt(postIndex));
        order.setTotal(total);
        order.setDateTime(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPerfumeId(cartItem.getPerfumeId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setPerfumeTitle(cartItem.getPerfumeTitle());
            orderItem.setPerfumeBrand(cartItem.getPerfumeBrand());
            orderItem.setPerfumeVolume(cartItem.getPerfumeVolume());
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);
        cartService.clearCart(userId);

        return order;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    public List<Order> getAllOrdersSortedByDateDesc() {
        return orderRepository.findAllByOrderByDateTimeDesc();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }
    
    public Long getTotalOrdersCount() {
        return orderRepository.count();
    }
    
    public BigDecimal getTotalRevenueFromOrders() {
        BigDecimal completed = orderRepository.sumTotalByStatus(OrderStatus.COMPLETED);
        return completed != null ? completed : BigDecimal.ZERO;
    }
    
    public BigDecimal getAverageOrderValue() {
        BigDecimal avg = orderRepository.averageTotalByStatus(OrderStatus.COMPLETED);
        return avg != null ? avg : BigDecimal.ZERO;
    }
    
    public Map<OrderStatus, Long> getOrderCountsByStatus() {
        Map<OrderStatus, Long> counts = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status, orderRepository.countByStatus(status));
        }
        return counts;
    }
    
    public Map<OrderStatus, BigDecimal> getOrderAmountsByStatus() {
        Map<OrderStatus, BigDecimal> amounts = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            BigDecimal amount = orderRepository.sumTotalByStatus(status);
            amounts.put(status, amount != null ? amount : BigDecimal.ZERO);
        }
        return amounts;
    }
}

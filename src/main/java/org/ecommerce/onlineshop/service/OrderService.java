package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.exeptions.FieldValidationException;
import org.ecommerce.onlineshop.repository.OrderRepository;
import org.ecommerce.onlineshop.utils.FieldsValidationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

        orderRepository.save(order);
        cartService.clearCart(userId);

        return order;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}

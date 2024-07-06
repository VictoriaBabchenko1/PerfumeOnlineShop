package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.exeptions.FieldValidationException;
import org.ecommerce.onlineshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, cartService);
    }

    @Test
    void createOrderTest_ValidInput_ReturnsOrder() {
        Long userId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String city = "New York";
        String email = "john.doe@example.com";
        String phoneNumber = "1234567890";
        String postIndex = "12345";

        CartItem cartItem1 = new CartItem(1L, 1L, 2, BigDecimal.TEN.multiply(BigDecimal.valueOf(2)));
        CartItem cartItem2 = new CartItem(1L, 2L, 3, BigDecimal.TEN.multiply(BigDecimal.valueOf(3)));

        when(cartService.getCartItems(userId)).thenReturn(List.of(cartItem1, cartItem2));
        when(cartService.calculateTotalPrice(List.of(cartItem1, cartItem2))).thenReturn(BigDecimal.valueOf(50));

        Order order = orderService.createOrder(userId, firstName, lastName, city, email, phoneNumber, postIndex);

        assertNotNull(order);
        assertEquals(userId, order.getUserId());
        assertEquals(firstName, order.getFirstName());
        assertEquals(lastName, order.getLastName());
        assertEquals(city, order.getCity());
        assertEquals(email, order.getEmail());
        assertEquals(phoneNumber, order.getPhoneNumber());
        assertEquals(Integer.parseInt(postIndex), order.getPostIndex());
        assertEquals(BigDecimal.valueOf(50), order.getTotal());
        assertNotNull(order.getDateTime());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService, times(1)).clearCart(userId);
    }

    @ParameterizedTest
    @CsvSource({
        "invalid_email, 1234567890, 12345",
        "john.doe@example.com, invalid_phone_number, 12345",
        "john.doe@example.com, 1234567890, invalid_post_index"
    })
    void createOrderTest_InvalidFields_ThrowsFieldValidationException(String email, String phoneNumber, String postIndex) {
        Long userId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String city = "New York";

        assertThrows(FieldValidationException.class,
            () -> orderService.createOrder(userId, firstName, lastName, city, email, phoneNumber, postIndex));

        verify(cartService, never()).getCartItems(anyLong());
        verify(cartService, never()).calculateTotalPrice(anyList());
        verify(orderRepository, never()).save(any(Order.class));
        verify(cartService, never()).clearCart(anyLong());
    }

    @Test
    void getOrdersByUserIdTest_ReturnsOrders() {
        Long userId = 1L;
        Order order1 = new Order();
        order1.setId(1L);
        order1.setUserId(userId);
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(userId);
        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findByUserId(userId)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(orders, result);
        verify(orderRepository, times(1)).findByUserId(userId);
    }
}

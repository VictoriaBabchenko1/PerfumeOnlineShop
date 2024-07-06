package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PerfumeService perfumeService;

    @BeforeEach
    public void setUp() {
        cartService = new CartService(userRepository, perfumeService);
    }

    @Test
    void addCartItemTest() {
        User user = new User();
        user.setId(1L);
        user.setCartItems(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(perfumeService.getPerfumeById(1L)).thenReturn(new Perfume("Test Title", "Test Brand",
            2000, "Test Country", "Test Gender", "Test Description",
            BigDecimal.TEN, 50, "Test Type", "Test Fragrance Notes"));

        cartService.addCartItem(1L, 1L, 2);

        CartItem cartItem = user.getCartItems().get(0);
        assertEquals(1L, cartItem.getUserId());
        assertEquals(1L, cartItem.getPerfumeId());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(20), cartItem.getPrice());

        verify(userRepository, times(1)).findById(1L);
        verify(perfumeService, times(1)).getPerfumeById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addCartItemTest_PerfumeIsNull() {
        User user = new User();
        user.setId(1L);
        user.setCartItems(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(perfumeService.getPerfumeById(1L)).thenReturn(null);

        cartService.addCartItem(1L, 1L, 2);

        verify(userRepository, times(1)).findById(1L);
        verify(perfumeService, times(1)).getPerfumeById(1L);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void addCartItemTest_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.addCartItem(1L, 1L, 2));

        verify(userRepository, times(1)).findById(1L);
        verify(perfumeService, times(0)).getPerfumeById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updateCartItemTest() {
        User user = new User();
        user.setId(1L);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem(1L, 1L, 2, BigDecimal.TEN.multiply(BigDecimal.valueOf(2)));
        cartItem.setId(1L);
        cartItems.add(cartItem);
        user.setCartItems(cartItems);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(perfumeService.getPerfumeById(1L)).thenReturn(new Perfume("Test Title", "Test Brand",
            2000,"Test Country", "Test Gender", "Test Description",
            BigDecimal.TEN, 50, "Test Type", "Test Fragrance Notes"));

        cartService.updateCartItem(1L, 1L, 3);

        CartItem updatedItem = user.getCartItems().get(0);
        assertEquals(1L, updatedItem.getUserId());
        assertEquals(1L, updatedItem.getPerfumeId());
        assertEquals(3, updatedItem.getQuantity());
        assertEquals(BigDecimal.valueOf(30), updatedItem.getPrice());

        verify(userRepository, times(1)).findById(1L);
        verify(perfumeService, times(1)).getPerfumeById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateCartItemTest_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.updateCartItem(1L, 1L, 2));

        verify(userRepository, times(1)).findById(1L);
        verify(perfumeService, times(0)).getPerfumeById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void removeCartItemTest() {
        User user = new User();
        user.setId(1L);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem(1L, 1L, 2, BigDecimal.TEN.multiply(BigDecimal.valueOf(2)));
        cartItem.setId(1L);
        cartItems.add(cartItem);
        user.setCartItems(cartItems);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        cartService.removeCartItem(1L, 1L);

        assertEquals(0, user.getCartItems().size());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void removeCartItemTest_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.removeCartItem(1L, 1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void getCartItemsTest() {
        User user = new User();
        user.setId(1L);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, 1L, 2, BigDecimal.TEN.multiply(BigDecimal.valueOf(2))));
        cartItems.add(new CartItem(1L, 2L, 3, BigDecimal.TEN.multiply(BigDecimal.valueOf(3))));
        user.setCartItems(cartItems);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<CartItem> result = cartService.getCartItems(1L);

        assertEquals(cartItems.size(), result.size());
        assertEquals(cartItems, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getCartItemsTest_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.getCartItems(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void calculateTotalPriceTest() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, 1L, 2, BigDecimal.TEN.multiply(BigDecimal.valueOf(2))));
        cartItems.add(new CartItem(1L, 2L, 3, BigDecimal.TEN.multiply(BigDecimal.valueOf(3))));

        BigDecimal totalPrice = cartService.calculateTotalPrice(cartItems);

        assertEquals(BigDecimal.valueOf(50), totalPrice);
    }

    @Test
    void clearCartTest() {
        User user = new User();
        user.setId(1L);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, 1L, 2, BigDecimal.TEN.multiply(BigDecimal.valueOf(2))));
        cartItems.add(new CartItem(1L, 2L, 3, BigDecimal.TEN.multiply(BigDecimal.valueOf(3))));
        user.setCartItems(cartItems);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        cartService.clearCart(1L);

        assertEquals(0, user.getCartItems().size());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void clearCartTest_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.clearCart(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(any(User.class));
    }
}

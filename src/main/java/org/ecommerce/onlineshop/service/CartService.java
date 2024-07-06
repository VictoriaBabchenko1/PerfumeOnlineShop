package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final UserRepository userRepository;
    private final PerfumeService perfumeService;

    public CartService(UserRepository userRepository, PerfumeService perfumeService) {
        this.userRepository = userRepository;
        this.perfumeService = perfumeService;
    }

    public void addCartItem(Long userId, Long perfumeId, Integer quantity) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Perfume perfume = perfumeService.getPerfumeById(perfumeId);
        if (perfume != null) {
            BigDecimal price = perfume.price();
            List<CartItem> cartItems = user.getCartItems();
            Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getPerfumeId().equals(perfumeId))
                .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
                item.setPrice(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            } else {
                CartItem newItem = new CartItem(userId, perfumeId, quantity, price.multiply(BigDecimal.valueOf(quantity)));
                newItem.setPerfumeTitle(perfume.title());
                newItem.setPerfumeBrand(perfume.brand());
                newItem.setPerfumeVolume(perfume.volume());
                cartItems.add(newItem);
            }

            userRepository.save(user);
        }
    }

    public void updateCartItem(Long userId, Long itemId, Integer quantity) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<CartItem> cartItems = user.getCartItems();
        Optional<CartItem> existingItem = cartItems.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (quantity <= 0) {
                cartItems.remove(item);
            } else {
                Perfume perfume = perfumeService.getPerfumeById(item.getPerfumeId());
                if (perfume != null) {
                    item.setQuantity(quantity);
                    item.setPrice(perfume.price().multiply(BigDecimal.valueOf(quantity)));
                }
            }
        }

        userRepository.save(user);
    }

    public void removeCartItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<CartItem> cartItems = user.getCartItems();
        cartItems.removeIf(item -> item.getId().equals(itemId));

        userRepository.save(user);
    }

    public List<CartItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getCartItems();
    }

    public BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
            .map(CartItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clearCart(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getCartItems().clear();
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}

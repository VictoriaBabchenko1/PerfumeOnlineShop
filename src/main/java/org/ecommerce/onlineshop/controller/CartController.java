package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.service.CartService;
import org.ecommerce.onlineshop.service.PerfumeService;
import org.ecommerce.onlineshop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    private final CartService cartService;
    private final PerfumeService perfumeService;
    private final UserService userService;

    public CartController(CartService cartService, PerfumeService perfumeService, UserService userService) {
        this.cartService = cartService;
        this.perfumeService = perfumeService;
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        List<CartItem> cartItems = cartService.getCartItems(userId);
        BigDecimal total = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("perfumeId") Long perfumeId,
                            @RequestParam("quantity") Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        Perfume perfume = perfumeService.getPerfumeById(perfumeId);
        if (perfume != null) {
            cartService.addCartItem(userId, perfumeId, quantity);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("itemId") Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        cartService.removeCartItem(userId, itemId);

        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    @ResponseBody
    public Map<String, Object> updateCartItem(@RequestParam("itemId") Long itemId,
                                 @RequestParam("quantity") Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        return cartService.updateAndGetCartSummary(userId, itemId, quantity);
    }
}

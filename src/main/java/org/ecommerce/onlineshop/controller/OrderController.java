package org.ecommerce.onlineshop.controller;

import com.stripe.exception.StripeException;
import org.ecommerce.onlineshop.domain.CartItem;
import org.ecommerce.onlineshop.domain.ChargeRequest;
import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.service.CartService;
import org.ecommerce.onlineshop.service.OrderService;
import org.ecommerce.onlineshop.service.StripeService;
import org.ecommerce.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;
    private final StripeService paymentsService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, StripeService stripeService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.paymentsService = stripeService;
        this.cartService = cartService;
    }

    @GetMapping("/order/fillForm")
    public String getOrderForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        List<CartItem> cartItems = cartService.getCartItems(userId);
        BigDecimal total = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("amount", total.intValue() * 100);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.USD);
        return "orderForm";
    }

    @PostMapping("/order/result")
    public String createOrder(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String city,
                              @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String postIndex, ChargeRequest chargeRequest, Model model)
        throws StripeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        chargeRequest.setDescription("charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.USD);
        paymentsService.charge(chargeRequest);

        try {
            Order order = orderService.createOrder(userId, firstName, lastName, city, email, phoneNumber, postIndex);
            model.addAttribute("order", order);
            return "orderResult";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "orderForm";
        }
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "orderResult";
    }
}

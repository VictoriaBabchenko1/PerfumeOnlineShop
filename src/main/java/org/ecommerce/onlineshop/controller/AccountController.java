package org.ecommerce.onlineshop.controller;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.service.OrderService;
import org.ecommerce.onlineshop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    private final UserService userService;
    private final OrderService orderService;

    public AccountController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/account")
    public String accountPage(@AuthenticationPrincipal User user, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Long userId = userService.findUserIdByUsername(currentPrincipalName);

        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersByUserId(userId));

        return "account";
    }
}

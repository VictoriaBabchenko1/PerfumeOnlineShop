package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.domain.Order;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.service.OrderService;
import org.ecommerce.onlineshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private Model model;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        this.accountController = new AccountController(userService, orderService);
    }

    @Test
    void accountPage_shouldRedirectToLogin_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account"))
            .andExpect(status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    void testAccountPageWithAuthentication() {
        User user = new User();
        user.setId(1L);

        when(userService.findUserIdByUsername("test")).thenReturn(1L);

        List<Order> orders = new ArrayList<>();
        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String viewName = accountController.accountPage(null, model);

        assertEquals("account", viewName);
        verify(userService, times(1)).findUserIdByUsername("test");
        verify(orderService, times(1)).getOrdersByUserId(1L);
    }
}

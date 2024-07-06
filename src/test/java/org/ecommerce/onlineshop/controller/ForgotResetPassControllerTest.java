package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.exeptions.ForgotResetPassException;
import org.ecommerce.onlineshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ForgotResetPassControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showForgotPasswordFormTest() throws Exception {
        mockMvc.perform(get("/forgot-password"))
            .andExpect(status().isOk())
            .andExpect(view().name("forgot-password"));
    }

    @Test
    void processForgotPasswordFormTest() throws Exception {
        String email = "test@example.com";
        String successMessage = "An email with reset instructions has been sent";

        Mockito.when(userService.forgotPass(email)).thenReturn(successMessage);

        mockMvc.perform(MockMvcRequestBuilders.post("/forgot-password")
                .param("email", email))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/forgot-password"))
            .andExpect(flash().attributeExists("success"));
    }

    @Test
    void processForgotPasswordFormTest_throwsException() throws Exception {
        Mockito.when(userService.forgotPass(Mockito.anyString())).thenThrow(new ForgotResetPassException("Test exception"));

        mockMvc.perform(MockMvcRequestBuilders.post("/forgot-password")
                .param("email", "test@example.com"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/forgot-password"))
            .andExpect(flash().attributeExists("error"));
    }

    @Test
    void showResetPasswordFormTest() throws Exception {
        String token = "valid-token";

        mockMvc.perform(get("/reset-password")
                .param("token", token))
            .andExpect(status().isOk())
            .andExpect(view().name("reset-password"));
    }

    @Test
    void processResetPasswordFormTest() throws Exception {
        String token = "valid-token";
        String password = "newpassword";

        Mockito.doNothing().when(userService).resetPass(token, password, password);

        mockMvc.perform(MockMvcRequestBuilders.post("/reset-password")
                .param("token", token)
                .param("password", password)
                .param("confirmPassword", password))
            .andExpect(status().isOk())
            .andExpect(view().name("reset-password-success"));
    }

    @Test
    void processResetPasswordFormTest_throwsException() throws Exception {
        String token = "valid-token";
        String password = "newpassword";
        String confirmPassword = "newpassword";
        String errorMessage = "Invalid token";

        Mockito.doThrow(new ForgotResetPassException(errorMessage)).when(userService).resetPass(token, password, confirmPassword);

        mockMvc.perform(MockMvcRequestBuilders.post("/reset-password")
                .param("token", token)
                .param("password", password)
                .param("confirmPassword", confirmPassword))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/reset-password?token=valid-token"))
            .andExpect(flash().attribute("error", errorMessage));
    }
}

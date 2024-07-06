package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.exeptions.RegistrationException;
import org.ecommerce.onlineshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registrationTest() throws Exception {
        mockMvc.perform(get("/registration"))
            .andExpect(status().isOk())
            .andExpect(view().name("registration"))
            .andExpect(model().attributeExists("userForm"));
    }

    @Test
    void addUserTest() throws Exception {
        User validUser = new User();
        validUser.setUsername("test");
        validUser.setEmail("testvalid@email.com");
        validUser.setPassword("password");
        validUser.setPasswordConfirm("password");

        Mockito.doNothing().when(userService).saveUser(any());

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", validUser.getUsername())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("passwordConfirm", validUser.getPasswordConfirm())
                .flashAttr("userForm", validUser))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    void addUserTest_WhenUserExists() throws Exception {
        User validUser = new User();
        validUser.setUsername("test");
        validUser.setEmail("testvalid@email.com");
        validUser.setPassword("password");
        validUser.setPasswordConfirm("password");

        Mockito.doThrow(new RegistrationException("User already exists")).when(userService).saveUser(any());

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", validUser.getUsername())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("passwordConfirm", validUser.getPasswordConfirm())
                .flashAttr("userForm", validUser))
            .andExpect(status().isOk())
            .andExpect(view().name("registration"))
            .andExpect(model().attributeExists("registrationError"))
            .andExpect(model().attribute("registrationError", "User already exists"));
    }
}

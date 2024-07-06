package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.service.PerfumeService;
import org.ecommerce.onlineshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private PerfumeService perfumeService;

    @InjectMocks
    private AdminController adminController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void showAllPerfumesTest() throws Exception {
        Perfume perfume1 = new Perfume("Test Title", "Test Brand", 2000,
            "Test Country", "Test Gender", "Test Description",
            new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes");

        Perfume perfume2 = new Perfume("Test Title", "Test Brand", 2000,
            "Test Country", "Test Gender", "Test Description",
            new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes");

        List<Perfume> perfumeList = Arrays.asList(perfume1, perfume2);

        when(perfumeService.getAllPerfumes()).thenReturn(perfumeList);

        mockMvc.perform(get("/admin/perfumes"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-perfumes"))
            .andExpect(model().attribute("perfumes", perfumeList));

        verify(perfumeService, times(1)).getAllPerfumes();
        verifyNoMoreInteractions(perfumeService);
    }

    @Test
    void deletePerfumeTest() throws Exception {
        Long perfumeId = 1L;
        when(perfumeService.deletePerfume(perfumeId)).thenReturn("Perfume deleted successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/perfumes/delete")
                .param("perfumeId", perfumeId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-perfumes"))
            .andExpect(model().attribute("message", "Perfume deleted successfully!"))
            .andExpect(model().attributeExists("perfumes"));

        verify(perfumeService, times(1)).deletePerfume(perfumeId);
        verify(perfumeService, times(1)).getAllPerfumes();
        verifyNoMoreInteractions(perfumeService);
    }

    @Test
    void showAddPerfumeFormTest() throws Exception {
        mockMvc.perform(get("/admin/perfume/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-add-perfume"));
    }

    @Test
    void addPerfumeTest() throws Exception {
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        String title = "Test Title";
        String brand = "Test Brand";
        String year = "2000";
        String country = "Test Country";
        String gender = "Test Gender";
        String description = "Test Description";
        String price = "50.00";
        String volume = "50";
        String type = "Test Type";
        String fragranceNotes = "Test Fragrance Notes";

        when(perfumeService.savePerfume(any(), any())).thenReturn("Perfume added successfully!");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/perfume/add")
                .file("image", image.getBytes())
                .param("title", title)
                .param("brand", brand)
                .param("year", year)
                .param("country", country)
                .param("gender", gender)
                .param("description", description)
                .param("price", price)
                .param("volume", volume)
                .param("type", type)
                .param("fragranceNotes", fragranceNotes))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-perfumes"))
            .andExpect(model().attribute("message", "Perfume added successfully!"))
            .andExpect(model().attributeExists("perfumes"));

        verify(perfumeService, times(1)).savePerfume(any(), any());
        verify(perfumeService, times(1)).getAllPerfumes();
        verifyNoMoreInteractions(perfumeService);
    }

    @Test
    void userListTest() throws Exception {
        List<User> userList = Arrays.asList(new User(), new User());

        when(userService.allUsers()).thenReturn(userList);

        mockMvc.perform(get("/admin/users"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-users"))
            .andExpect(model().attribute("allUsers", userList));

        verify(userService, times(1)).allUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUserTest() throws Exception {
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn("User deleted successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/delete")
                .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-users"))
            .andExpect(model().attribute("message", "User deleted successfully!"))
            .andExpect(model().attributeExists("allUsers"));

        verify(userService, times(1)).deleteUser(userId);
        verify(userService, times(1)).allUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void resetUserPasswordTest() throws Exception {
        Long userId = 1L;
        when(userService.resetUserPassword(userId)).thenReturn("User password reset successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/reset-password")
                .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-users"))
            .andExpect(model().attribute("message", "User password reset successfully!"))
            .andExpect(model().attributeExists("allUsers"));

        verify(userService, times(1)).resetUserPassword(userId);
        verify(userService, times(1)).allUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void showAllAdminsTest() throws Exception {
        List<User> adminList = Arrays.asList(new User(), new User());

        when(userService.allAdmins()).thenReturn(adminList);

        mockMvc.perform(get("/admin/superAdmin/admins"))
            .andExpect(status().isOk())
            .andExpect(view().name("superAdmin-allAdmins"))
            .andExpect(model().attribute("admins", adminList));

        verify(userService, times(1)).allAdmins();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteAdminTest() throws Exception {
        Long adminId = 1L;
        when(userService.deleteUser(adminId)).thenReturn("Admin deleted successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/superAdmin/admins/delete")
                .param("userId", adminId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("superAdmin-allAdmins"))
            .andExpect(model().attribute("message", "Admin deleted successfully!"))
            .andExpect(model().attributeExists("admins"));

        verify(userService, times(1)).deleteUser(adminId);
        verify(userService, times(1)).allAdmins();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void resetAdminPasswordTest() throws Exception {
        Long adminId = 1L;
        when(userService.resetUserPassword(adminId)).thenReturn("Admin password reset successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/superAdmin/admins/reset-password")
                .param("userId", adminId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("superAdmin-allAdmins"))
            .andExpect(model().attribute("message", "Admin password reset successfully!"))
            .andExpect(model().attributeExists("admins"));

        verify(userService, times(1)).resetUserPassword(adminId);
        verify(userService, times(1)).allAdmins();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void promoteToAdminTest() throws Exception {
        Long userId = 1L;
        when(userService.promoteToAdmin(userId)).thenReturn("User promoted to admin successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/superAdmin/users/promote-to-admin")
                .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-users"))
            .andExpect(model().attribute("message", "User promoted to admin successfully!"))
            .andExpect(model().attributeExists("allUsers"));

        verify(userService, times(1)).promoteToAdmin(userId);
        verify(userService, times(1)).allUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void demoteToRegularUserTest() throws Exception {
        Long adminId = 1L;
        when(userService.demoteToRegularUser(adminId)).thenReturn("Admin demoted to user successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/superAdmin/admins/demote-to-user")
                .param("userId", adminId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("superAdmin-allAdmins"))
            .andExpect(model().attribute("message", "Admin demoted to user successfully!"))
            .andExpect(model().attributeExists("admins"));

        verify(userService, times(1)).demoteToRegularUser(adminId);
        verify(userService, times(1)).allAdmins();
        verifyNoMoreInteractions(userService);
    }
}

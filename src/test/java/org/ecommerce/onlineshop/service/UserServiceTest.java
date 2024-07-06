package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.Role;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.exeptions.FieldValidationException;
import org.ecommerce.onlineshop.exeptions.ForgotResetPassException;
import org.ecommerce.onlineshop.exeptions.RegistrationException;
import org.ecommerce.onlineshop.repository.RoleRepository;
import org.ecommerce.onlineshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(emailService, userRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    void loadUserByUsernameTest_ExistingUser_ShouldReturnUser() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameTest_UsernameNotFound_ShouldThrowException() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void findUserByIdTest_ExistingUser_ShouldReturnUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void findUserIdByUsernameTest_ExistingUsername_ShouldReturnUserId() {
        String username = "testUser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        Long userId = userService.findUserIdByUsername(username);

        assertEquals(1L, userId);
    }

    @Test
    void findUserIdByUsernameTest_NonExistingUsername_ShouldThrowException() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserIdByUsername(username));
    }

    @Test
    void allUsersTest_FilteringAdmins_ShouldReturnOnlyRegularUsers() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ROLE_ADMIN");

        Role superAdminRole = new Role();
        superAdminRole.setId(3L);
        superAdminRole.setName("ROLE_SUPER_ADMIN");

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1.setRoles(Collections.singleton(userRole));

        User admin1 = new User();
        admin1.setId(2L);
        admin1.setUsername("admin1");
        admin1.setPassword("password2");
        admin1.setEmail("admin1@example.com");
        admin1.setRoles(Collections.singleton(adminRole));

        User superAdmin1 = new User();
        superAdmin1.setId(3L);
        superAdmin1.setUsername("superAdmin1");
        superAdmin1.setPassword("password3");
        superAdmin1.setEmail("superAdmin1@example.com");
        superAdmin1.setRoles(Collections.singleton(superAdminRole));

        List<User> users = Arrays.asList(user1, admin1, superAdmin1);

        when(userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(users);

        List<User> regularUsers = userService.allUsers();

        assertEquals(1, regularUsers.size());
        assertEquals("user1", regularUsers.get(0).getUsername());
    }

    @Test
    void allAdminsTest_FilteringAdmins_ShouldReturnOnlyAdmins() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ROLE_ADMIN");

        Role superAdminRole = new Role();
        superAdminRole.setId(3L);
        superAdminRole.setName("ROLE_SUPER_ADMIN");

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1.setRoles(Collections.singleton(userRole));

        User admin1 = new User();
        admin1.setId(2L);
        admin1.setUsername("admin1");
        admin1.setPassword("password2");
        admin1.setEmail("admin1@example.com");
        admin1.setRoles(Collections.singleton(adminRole));

        User superAdmin1 = new User();
        superAdmin1.setId(3L);
        superAdmin1.setUsername("superAdmin1");
        superAdmin1.setPassword("password3");
        superAdmin1.setEmail("superAdmin1@example.com");
        superAdmin1.setRoles(Collections.singleton(superAdminRole));

        List<User> users = Arrays.asList(user1, admin1, superAdmin1);

        when(userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(users);

        List<User> admins = userService.allAdmins();

        assertEquals(1, admins.size());
        assertEquals("admin1", admins.get(0).getUsername());
    }

    @Test
    void saveUserTest_ValidUser_ShouldSaveUser() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password");
        newUser.setPasswordConfirm("password");
        newUser.setRoles(Collections.singleton(userRole));

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);

        userService.saveUser(newUser);

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void saveUserTest_EmptyFields_ShouldThrowException() {
        User user = new User();
        assertThrows(FieldValidationException.class, () -> userService.saveUser(user));
    }

    @Test
    void saveUserTest_InvalidUsername_ShouldThrowException() {
        User user = new User();
        user.setUsername("user@name");
        assertThrows(FieldValidationException.class, () -> userService.saveUser(user));
    }

    @Test
    void saveUserTest_InvalidEmailFormat_ShouldThrowException() {
        User user = new User();
        user.setEmail("invalid.email");
        assertThrows(FieldValidationException.class, () -> userService.saveUser(user));
    }

    @Test
    void saveUserTest_PasswordsDoNotMatch_ShouldThrowException() {
        User user = new User();
        user.setPassword("password1");
        user.setPasswordConfirm("password2");
        assertThrows(FieldValidationException.class, () -> userService.saveUser(user));
    }

    @Test
    void saveUserTest_DuplicateUsername_ShouldThrowException() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existinguser@example.com");
        existingUser.setPassword("password");
        existingUser.setPasswordConfirm("password");
        existingUser.setRoles(Collections.singleton(userRole));

        User newUser = new User();
        newUser.setUsername("existingUser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password");
        newUser.setPasswordConfirm("password");
        newUser.setRoles(Collections.singleton(userRole));

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(existingUser);

        assertThrows(RegistrationException.class, () -> userService.saveUser(newUser));
    }

    @Test
    void saveUserTest_DuplicateEmail_ShouldThrowException() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("user1");
        existingUser.setEmail("newuser@example.com");
        existingUser.setPassword("password");
        existingUser.setPasswordConfirm("password");
        existingUser.setRoles(Collections.singleton(userRole));

        User newUser = new User();
        newUser.setUsername("user2");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password");
        newUser.setPasswordConfirm("password");
        newUser.setRoles(Collections.singleton(userRole));

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(existingUser);

        assertThrows(RegistrationException.class, () -> userService.saveUser(newUser));
    }

    @Test
    void deleteUserTest_UserExists_ShouldReturnSuccessMessage() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        String result = userService.deleteUser(userId);

        assertEquals("User with ID 1 successfully deleted", result);
    }

    @Test
    void deleteUserTest_UserDoesNotExist_ShouldReturnNotFoundMessage() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        String result = userService.deleteUser(userId);

        assertEquals("User not found", result);
    }

    @Test
    void forgotPassTest_ValidEmail_ShouldSendEmailAndReturnSuccessMessage() {
        String email = "user@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        String result = userService.forgotPass(email);

        assertEquals("Check your email for password reset instructions", result);
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void forgotPassTest_InvalidEmail_ShouldThrowException() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(ForgotResetPassException.class, () -> userService.forgotPass(email));
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendSimpleMessage(anyString(), anyString(), anyString());
    }

    @Test
    void resetPassTest_ValidToken_ShouldResetPassword() {
        String token = "validToken";
        String password = "newPassword";
        String confirmPassword = "newPassword";

        User user = new User();
        user.setToken(token);
        user.setTokenCreationDate(LocalDateTime.now());

        when(userRepository.findByToken(token)).thenReturn(user);

        userService.resetPass(token, password, confirmPassword);

        assertNull(user.getToken());
        assertNull(user.getTokenCreationDate());
        assertEquals(bCryptPasswordEncoder.encode(password), user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetPassTest_PasswordsDoNotMatch_ShouldThrowException() {
        String token = "validToken";
        String password = "newPassword";
        String confirmPassword = "invalidPassword";

        User user = new User();
        user.setToken(token);
        user.setTokenCreationDate(LocalDateTime.now());

        assertThrows(FieldValidationException.class, () -> userService.resetPass(token, password, confirmPassword));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetPassTest_InvalidToken_ShouldThrowException() {
        String token = "invalidToken";
        String password = "newPassword";
        String confirmPassword = "newPassword";

        when(userRepository.findByToken(token)).thenReturn(null);

        assertThrows(ForgotResetPassException.class, () -> userService.resetPass(token, password, confirmPassword));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetPassTest_ExpiredToken_ShouldThrowException() {
        String token = "expiredToken";
        String password = "newPassword";
        String confirmPassword = "newPassword";

        User user = new User();
        user.setToken(token);
        user.setTokenCreationDate(LocalDateTime.now().minusHours(2));

        when(userRepository.findByToken(token)).thenReturn(user);

        assertThrows(ForgotResetPassException.class, () -> userService.resetPass(token, password, confirmPassword));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetUserPasswordTest_ValidUser_ShouldResetPassword() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = userService.resetUserPassword(userId);

        assertNotNull(result);
        assertTrue(result.contains("Password reset successfully"));
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendSimpleMessage(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void resetUserPasswordTest_UserNotFound_ShouldThrowException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.resetUserPassword(userId));
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendSimpleMessage(anyString(), anyString(), anyString());
    }

    @Test
    void promoteToAdminTest_UserNotAdmin_ShouldPromoteToAdmin() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");
        user.setRoles(new HashSet<>(Collections.singleton(new Role(1L, "ROLE_USER"))));

        Role adminRole = new Role(2L, "ROLE_ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);

        String result = userService.promoteToAdmin(userId);

        assertEquals("User with ID 1 was successfully promoted to Admin", result);
        assertTrue(user.getRoles().contains(adminRole));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void promoteToAdminTest_UsernameNotFoundException_ShouldThrowException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.promoteToAdmin(userId));
    }

    @Test
    void promoteToAdminTest_UserAlreadyAdmin_ShouldThrowException() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("admin1");
        user.setRoles(new HashSet<>(Collections.singleton(new Role(2L, "ROLE_ADMIN"))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(new Role(2L, "ROLE_ADMIN"));

        assertThrows(FieldValidationException.class, () -> userService.promoteToAdmin(userId));
        verify(userRepository, never()).save(any());
    }

    @Test
    void demoteToRegularUserTest_UserIsAdmin_ShouldDemoteToRegularUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("admin1");
        user.setRoles(new HashSet<>(Collections.singleton(new Role(2L, "ROLE_ADMIN"))));

        Role adminRole = new Role(2L, "ROLE_ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);

        String result = userService.demoteToRegularUser(userId);

        assertEquals("User with ID 1 was successfully demoted to Regular User", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void demoteToRegularUserTest_UsernameNotFoundException_ShouldThrowException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.demoteToRegularUser(userId));
    }

    @Test
    void demoteToRegularUserTest_UserIsNotAdmin_ShouldThrowException() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setRoles(new HashSet<>(Collections.singleton(new Role(1L, "ROLE_USER"))));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(new Role(1L, "ROLE_USER"));

        assertThrows(FieldValidationException.class, () -> userService.promoteToAdmin(userId));
        verify(userRepository, never()).save(any());
    }
}

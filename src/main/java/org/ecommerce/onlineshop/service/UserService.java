package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.Role;
import org.ecommerce.onlineshop.domain.User;
import org.ecommerce.onlineshop.exeptions.FieldValidationException;
import org.ecommerce.onlineshop.exeptions.ForgotResetPassException;
import org.ecommerce.onlineshop.exeptions.RegistrationException;
import org.ecommerce.onlineshop.repository.RoleRepository;
import org.ecommerce.onlineshop.repository.UserRepository;
import org.ecommerce.onlineshop.utils.FieldsValidationUtils;
import org.ecommerce.onlineshop.utils.RandomPasswordGenerator;
import org.ecommerce.onlineshop.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService (EmailService emailService, UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);

        return userFromDb.orElse(new User());
    }

    public Long findUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.getId();
    }

    public List<User> allUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return users.stream()
            .filter(user -> user.getRoles().stream()
                .noneMatch(role -> role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_SUPER_ADMIN")))
            .toList();
    }

    public List<User> allAdmins() {
        List<User> admins = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return admins.stream()
            .filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"))
                && user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_SUPER_ADMIN")))
            .toList();
    }

    public void saveUser(User user) {
        if (!FieldsValidationUtils.isAllFieldsFilled(user)) {
            throw new FieldValidationException("All fields must be filled in");
        }

        if (!FieldsValidationUtils.isValidUsername(user.getUsername())) {
            throw new FieldValidationException("Username should contain only letters and digits");
        }

        if (!FieldsValidationUtils.isValidEmail(user.getEmail())) {
            throw new FieldValidationException("Invalid email format");
        }

        if (!FieldsValidationUtils.doPasswordsMatch(user)){
            throw new FieldValidationException("Passwords do not match");
        }

        User userFromDB = userRepository.findByUsername(user.getUsername());
        User userFromDBByEmail = userRepository.findByEmail(user.getEmail());

        if (userFromDB != null) {
            throw new RegistrationException("User with the same username already exists");
        }

        if (userFromDBByEmail != null) {
            throw new RegistrationException("User with the same email already exists");
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return "User with ID " + userId + " successfully deleted";
        }

        return "User not found";
    }

    public String forgotPass(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if(userOptional.isEmpty()){
            throw new ForgotResetPassException("Invalid email");
        }

        User user = userOptional.get();
        String token = TokenUtils.generateToken();
        user.setToken(token);
        user.setTokenCreationDate(LocalDateTime.now());

        userRepository.save(user);

        String subject = "Password Reset";
        String text = "To reset your password, please use the following link: http://localhost:8888/reset-password?token=" + token;

        emailService.sendSimpleMessage(email, subject, text);

        return "Check your email for password reset instructions";
    }

    public void resetPass(String token, String password, String confirmPassword){
        if (!FieldsValidationUtils.doPasswordsMatch(password, confirmPassword)) {
            throw new FieldValidationException("Passwords do not match");
        }

        Optional<User> userOptional= Optional.ofNullable(userRepository.findByToken(token));

        if(userOptional.isEmpty()){
            throw new ForgotResetPassException("Invalid token");
        }

        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (TokenUtils.isTokenExpired(tokenCreationDate)) {
            throw new ForgotResetPassException("Token expired");
        }

        User user = userOptional.get();

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);
    }

    public String resetUserPassword(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newPassword = RandomPasswordGenerator.generateRandomPassword(10);

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);

        String subject = "Password Reset";
        String text = "Your password has been reset. Your new password is: " + newPassword +
            ". This is a system-generated password. No one knows it except you.";
        emailService.sendSimpleMessage(user.getEmail(), subject, text);

        return "Password reset successfully. Email sent to user with ID " + userId;
    }

    public String promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if (user.getRoles().stream().anyMatch(role -> role.getId().equals(adminRole.getId()))) {
            throw new FieldValidationException("User is already an admin");
        }

        user.getRoles().add(adminRole);
        userRepository.save(user);

        return "User with ID " + userId + " was successfully promoted to Admin";
    }

    public String demoteToRegularUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if (user.getRoles().stream().noneMatch(role -> role.getId().equals(adminRole.getId()))) {
            throw new FieldValidationException("User is not an admin");
        }

        user.getRoles().remove(adminRole);
        userRepository.save(user);

        return "User with ID " + userId + " was successfully demoted to Regular User";
    }
}

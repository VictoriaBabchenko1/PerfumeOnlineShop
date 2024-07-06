package org.ecommerce.onlineshop.utils;
import org.ecommerce.onlineshop.domain.User;

public class FieldsValidationUtils {
    private FieldsValidationUtils() {
    }
    public static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]+$");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean doPasswordsMatch (User user) {
        return user.getPassword().equals(user.getPasswordConfirm());
    }

    public static boolean doPasswordsMatch (String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    public static boolean isAllFieldsFilled(User user) {
        return  user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty() &&
                user.getPasswordConfirm() != null && !user.getPasswordConfirm().isEmpty();
    }

    public static boolean doesContainOnlyDigits(String postIndex) {
        return postIndex.matches("\\d+");
    }
}

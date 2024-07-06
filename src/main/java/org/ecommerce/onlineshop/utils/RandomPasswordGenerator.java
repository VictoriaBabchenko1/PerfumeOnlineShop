package org.ecommerce.onlineshop.utils;
import java.security.SecureRandom;

public class RandomPasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    private static final String PASSWORD_ALLOW =
        CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final SecureRandom random = new SecureRandom();

    private RandomPasswordGenerator() {

    }

    public static String generateRandomPassword(int length) {
        if (length < 7) {
            throw new IllegalArgumentException("Password length must be at least 7 characters");
        }

        StringBuilder password = new StringBuilder(length);
        password.append(randomChar(CHAR_LOWER));
        password.append(randomChar(CHAR_LOWER));
        password.append(randomChar(CHAR_UPPER));
        password.append(randomChar(CHAR_UPPER));
        password.append(randomChar(NUMBER));
        password.append(randomChar(NUMBER));
        password.append(randomChar(OTHER_CHAR));

        for (int i = 7; i < length; i++) {
            password.append(randomChar(PASSWORD_ALLOW));
        }

        return password.toString();
    }

    private static char randomChar(String inputString) {
        int randomIndex = random.nextInt(inputString.length());

        return inputString.charAt(randomIndex);
    }
}

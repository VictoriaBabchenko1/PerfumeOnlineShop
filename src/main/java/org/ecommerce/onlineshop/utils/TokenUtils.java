package org.ecommerce.onlineshop.utils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class TokenUtils {
    private static final long EXPIRE_TOKEN=30;
    private TokenUtils() {

    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN;
    }
}

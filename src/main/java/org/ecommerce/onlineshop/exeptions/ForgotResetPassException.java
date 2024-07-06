package org.ecommerce.onlineshop.exeptions;

public class ForgotResetPassException extends RuntimeException {
    public ForgotResetPassException() {
        super();
    }

    public ForgotResetPassException(String message) {
        super(message);
    }

    public ForgotResetPassException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForgotResetPassException(Throwable cause) {
        super(cause);
    }
}

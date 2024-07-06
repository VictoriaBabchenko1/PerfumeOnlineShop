package org.ecommerce.onlineshop.exeptions;

public class FieldValidationException extends RuntimeException {
    public FieldValidationException() {
        super();
    }

    public FieldValidationException(String message) {
        super(message);
    }

    public FieldValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldValidationException(Throwable cause) {
        super(cause);
    }
}

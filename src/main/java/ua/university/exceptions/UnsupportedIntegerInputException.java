package ua.university.exceptions;

public class UnsupportedIntegerInputException extends AppException {
    public UnsupportedIntegerInputException(String message, Throwable cause) {
        super(message,cause);
    }

    public UnsupportedIntegerInputException(String message) {
        super(message);
    }
}

package ua.university.exceptions;

public class InitializationFailedException extends AppException {
    public InitializationFailedException(String message, Throwable cause) {
        super(message,cause);
    }

    public InitializationFailedException(String message) { super(message); }
}

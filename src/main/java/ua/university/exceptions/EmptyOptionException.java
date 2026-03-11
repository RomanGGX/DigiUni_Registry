package ua.university.exceptions;

public class EmptyOptionException extends AppException {
    public EmptyOptionException(String message, Throwable cause) {
        super(message,cause);
    }

    public EmptyOptionException(String message) {
        super(message);
    }
}

package ua.university.exceptions;

public class EmptyInputException extends  AppException {
    public EmptyInputException(String message, Throwable cause) {
        super(message,cause);
    }

    public EmptyInputException(String message) {
        super(message);
    }
}

package ua.university.exceptions;

public class LocalFileSaveException extends AppException {
    public LocalFileSaveException(String message, Throwable cause) { super(message,cause); }

    public LocalFileSaveException(String message) { super(message); }
}
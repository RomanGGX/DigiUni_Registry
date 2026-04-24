package ua.university.exceptions;

public class FileUpdateFailedException extends AppException {
    public FileUpdateFailedException(String message, Throwable cause) {
        super(message,cause);
    }

    public FileUpdateFailedException(String message) { super(message); }
}

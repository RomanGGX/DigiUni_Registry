package ua.university.exceptions;

public class ServerSaveException extends AppException {
    public ServerSaveException(String message, Throwable cause) { super(message,cause); }

    public ServerSaveException(String message) { super(message); }
}
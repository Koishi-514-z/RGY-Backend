package org.example.rgybackend.Utils;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }
}

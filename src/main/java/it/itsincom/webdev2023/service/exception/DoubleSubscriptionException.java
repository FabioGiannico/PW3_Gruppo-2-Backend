package it.itsincom.webdev2023.service.exception;

public class DoubleSubscriptionException extends RuntimeException {
    public DoubleSubscriptionException(String message) {
        super(message);
    }
}
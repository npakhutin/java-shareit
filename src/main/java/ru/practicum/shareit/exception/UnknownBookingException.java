package ru.practicum.shareit.exception;

public class UnknownBookingException extends RuntimeException {
    public UnknownBookingException(String m) {
        super(m);
    }
}

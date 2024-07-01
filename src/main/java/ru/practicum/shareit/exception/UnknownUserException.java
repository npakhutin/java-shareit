package ru.practicum.shareit.exception;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String m) {
        super(m);
    }
}

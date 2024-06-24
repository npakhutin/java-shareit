package ru.practicum.shareit.user.exception;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String m) {
        super(m);
    }
}

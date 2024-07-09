package ru.practicum.shareit.exception;

public class InsufficientPermissionException extends RuntimeException {
    public InsufficientPermissionException(String m) {
        super(m);
    }
}

package de.flockiix.flockbot.core.exception;

public class CommandAlreadyExistsException extends RuntimeException {
    public CommandAlreadyExistsException(String message) {
        super(message);
    }
}

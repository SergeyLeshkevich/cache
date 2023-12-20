package ru.clevertec.exception;

import java.util.UUID;

public class ValidationException extends RuntimeException{

    /**
     * @param uuid - product identifier
     */
    public ValidationException(UUID uuid) {
        super(String.format("Car with uuid: %s not found", uuid));
    }

    /**
     * @param message - exception message
     */
    public ValidationException(String message) {
        super(message);
    }
}


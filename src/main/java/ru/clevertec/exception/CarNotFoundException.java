package ru.clevertec.exception;

import java.util.UUID;

public class CarNotFoundException extends RuntimeException{

    /**
     * @param uuid - product identifier
     */
    public CarNotFoundException(UUID uuid) {
        super(String.format("Car with uuid: %s not found", uuid));
    }
}

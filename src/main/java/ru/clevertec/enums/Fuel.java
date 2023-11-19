package ru.clevertec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Fuel {

    PETROL("petrol"), DIESEL("diesel");

    @Getter
    private final String type;
}

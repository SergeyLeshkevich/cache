package ru.clevertec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BodyType {

    PICKUP("pickup"),SEDAN("sedan"),STATION_WAGON("station wagon");

    @Getter
    private final String type;
}

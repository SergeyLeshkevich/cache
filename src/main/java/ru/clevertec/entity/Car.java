package ru.clevertec.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    private UUID id;
    private String brand;
    private String model;
    private Enum<BodyType> bodyType;
    private double engineCapacity;
    private Enum<Fuel> fuelType;
}

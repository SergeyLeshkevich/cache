package ru.clevertec.entity.data;


import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;
import ru.clevertec.util.validation.annotation.Valid;

public record CarDTO(

        /**
         * {@link by.lechkevich.task_cache.entity.Car}
         */
        @Valid(regex = "^[a-zA-Z]{1,15}$")
        String brand,

        /**
         * {@link by.lechkevich.task_cache.entity.Car}
         */
        String model,

        /**
         * {@link by.lechkevich.task_cache.entity.Car}
         */
        Enum<BodyType> bodyType,

        /**
         * {@link by.lechkevich.task_cache.entity.Car}
         */
        double engineCapacity,

        /**
         * {@link by.lechkevich.task_cache.entity.Car}
         */
        Enum<Fuel> fuelType) {
}


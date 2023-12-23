package ru.clevertec.service;


import ru.clevertec.entity.Car;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.exception.CarNotFoundException;

import java.util.List;
import java.util.UUID;

public interface CarService {

    CarDTO get(UUID uuid) throws CarNotFoundException;

    List<CarDTO> getAll();

    UUID create(CarDTO carDto);

    UUID update(UUID uuid, CarDTO carDto) throws CarNotFoundException;

    void delete(UUID uuid);

    List<CarDTO> findLimitList(int limit, int numberStartSelection);
}

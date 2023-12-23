package ru.clevertec.dao;


import ru.clevertec.entity.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarDAO {

    UUID save(Car car);

    void delete(UUID uuid);

    List<Car> findAll();

    Optional<Car> findById(UUID uuid);
    List<Car> findLimitList(int limit, int numberStartSelection);

}

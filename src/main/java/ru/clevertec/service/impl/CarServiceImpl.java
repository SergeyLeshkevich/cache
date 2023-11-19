package ru.clevertec.service.impl;


import ru.clevertec.dao.CarDAO;
import ru.clevertec.entity.Car;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.mapper.CarMapper;
import ru.clevertec.exception.CarNotFoundException;
import ru.clevertec.service.CarService;

import java.util.List;
import java.util.UUID;

public class CarServiceImpl implements CarService {

    private final CarDAO carDAO;
    private final CarMapper carMapper;

    public CarServiceImpl(CarDAO carDAO, CarMapper carMapper) {
        this.carDAO = carDAO;
        this.carMapper = carMapper;
    }

    /**
     * Searches for a car from DAO by ID
     *
     * @param uuid vehicle identifier
     * @return found car
     * @throws CarNotFoundException if not found
     */
    @Override
    public CarDTO get(UUID uuid) throws CarNotFoundException {
        return carMapper.toCarDto(carDAO.findById(uuid).orElseThrow(() -> new CarNotFoundException(uuid)));
    }

    /**
     * Returns all existing cars
     *
     * @return a list with information about cars
     */
    @Override
    public List<CarDTO> getAll() {
        return carMapper.toCardDtoList(carDAO.findAll());
    }

    /**
     * Creates a new car from DTO
     *
     * @param carDto car without id
     * @return identifier of the created car
     */
    @Override
    public UUID create(CarDTO carDto) {
        UUID uuid = UUID.randomUUID();
        Car car = carMapper.fromCarDto(carDto);
        car.setId(uuid);
        return carDAO.save(car);
    }

    /**
     * Updates an existing vehicle from information received in the DTO
     *
     * @param uuid   vehicle identifier to update
     * @param carDto car without id
     * @return identifier of the created car
     */
    @Override
    public UUID update(UUID uuid, CarDTO carDto) {
        Car car = carMapper.fromCarDto(carDto);
        car.setId(uuid);
        return carDAO.save(car);
    }

    /**
     * Deletes an existing car
     *
     * @param uuid car identifier to delete
     */
    @Override
    public void delete(UUID uuid) {
        if (uuid != null) {
            carDAO.delete(uuid);
        }
    }
}

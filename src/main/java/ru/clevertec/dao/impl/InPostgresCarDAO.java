package ru.clevertec.dao.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.clevertec.util.constant.SQLConstants;
import ru.clevertec.dao.CarMapperDB;
import ru.clevertec.dao.CarDAO;
import ru.clevertec.entity.Car;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class InPostgresCarDAO implements CarDAO {

    private final NamedParameterJdbcTemplate template;

    /**
     * Saves or updates the car in memory
     *
     * @param car the car to save
     * @return saved car
     * @throws IllegalArgumentException if the passed vehicle is null
     */
    @Override
    public UUID save(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("id", car.getId());
        params.put("brand", car.getBrand());
        params.put("model", car.getModel());
        params.put("bodyType", car.getBodyType().name());
        params.put("engineCapacity", car.getEngineCapacity());
        params.put("fuelType", car.getFuelType().name());

        if (findById(car.getId()).isEmpty()) {
            return template.queryForObject(SQLConstants.SQL_SAVE, params, UUID.class);
        } else {
            return template.queryForObject(SQLConstants.SQL_UPDATE, params, UUID.class);
        }
    }

    /**
     * Removes a car from memory by identifier
     *
     * @param uuid vehicle identifier
     */
    @Override
    public void delete(UUID uuid) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", uuid);

        template.update(SQLConstants.SQL_DELETE, params);
    }

    /**
     * Searches all cars in the database
     *
     * @return list of found cars
     */
    @Override
    public List<Car> findAll() {
        return template.query(SQLConstants.SQL_FIND_ALL, new CarMapperDB());
    }

    /**
     * Searches the memory for a car by identifier
     *
     * @param uuid vehicle identifier
     * @return Optional<Car> if found, otherwise Optional.empty()
     */
    @Override
    public Optional<Car> findById(UUID uuid) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", uuid);

        return template.query(SQLConstants.SQL_FIND_BY_ID, params, new CarMapperDB()).stream().findAny();
    }
}

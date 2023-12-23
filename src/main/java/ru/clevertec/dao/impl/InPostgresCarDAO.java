package ru.clevertec.dao.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dao.CarDAO;
import ru.clevertec.dao.CarMapperDB;
import ru.clevertec.entity.Car;
import ru.clevertec.util.constant.SQLConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class InPostgresCarDAO implements CarDAO {

    public static final String ID = "id";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String BODY_TYPE = "bodyType";
    public static final String ENGINE_CAPACITY = "engineCapacity";
    public static final String FUEL_TYPE = "fuelType";
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
        params.put(ID, car.getId());
        params.put(BRAND, car.getBrand());
        params.put(MODEL, car.getModel());
        params.put(BODY_TYPE, car.getBodyType().name());
        params.put(ENGINE_CAPACITY, car.getEngineCapacity());
        params.put(FUEL_TYPE, car.getFuelType().name());

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
        params.put(ID, uuid);

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
        params.put(ID, uuid);

        return template.query(SQLConstants.SQL_FIND_BY_ID, params, new CarMapperDB()).stream().findAny();
    }

    @Override
    @Transactional
    public List<Car> findLimitList(int limit, int numberStartSelection) {
        String sqlFindLimitList = "SELECT * FROM cars LIMIT " + limit + " OFFSET " + numberStartSelection;

        return template.query(sqlFindLimitList, new CarMapperDB());
    }
}

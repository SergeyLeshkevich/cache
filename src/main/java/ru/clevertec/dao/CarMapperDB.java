package ru.clevertec.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.clevertec.entity.Car;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CarMapperDB implements RowMapper<Car> {

    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
        Car car = new Car();
        car.setId(UUID.fromString(rs.getString("id")));
        car.setModel(rs.getString("model"));
        car.setBrand(rs.getString("brand"));
        car.setEngineCapacity(rs.getDouble("engine_capacity"));
        car.setBodyType(BodyType.valueOf(rs.getString("body_type")));
        car.setFuelType(Fuel.valueOf(rs.getString("fuel_type")));
        return car;
    }
}

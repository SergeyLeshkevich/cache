package ru.clevertec.util.constant;

public class SQLConstants {
    public static final String SQL_SAVE = "INSERT INTO cars (id, brand, model, body_type, engine_capacity, fuel_type) " +
            "VALUES (:id, :brand, :model, :bodyType, :engineCapacity, :fuelType) RETURNING id";
    public static final String SQL_DELETE = "DELETE FROM cars WHERE id=:id";
    public static final String SQL_FIND_ALL = "SELECT * FROM cars";
    public static final String SQL_FIND_BY_ID = "SELECT * FROM cars WHERE id=:id";
    public static final String SQL_UPDATE = "UPDATE cars SET brand=:brand, " +
            "model=:model, " +
            "body_type=:bodyType, " +
            "engine_capacity=:engineCapacity, " +
            "fuel_type=:fuelType " +
            "WHERE id=:id " +
            "RETURNING id";
}

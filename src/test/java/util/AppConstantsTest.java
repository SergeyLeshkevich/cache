package util;

import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;

import java.util.UUID;

public class AppConstantsTest {
    public static final UUID UUID_CAR = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");
    public static final String BRAND_CAR = "Toyota";
    public static final String MODEL_CAR = "Hilux";
    public static final Enum<BodyType> BODY_TYPE_ENUM_CAR = BodyType.PICKUP;
    public static final double ENGINE_CAPACITY = 2.5;
    public static final Enum<Fuel> FUEL_ENUM_CAR = Fuel.DIESEL;
}


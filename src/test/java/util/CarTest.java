package util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.entity.Car;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@With
public class CarTest implements TestBuilder<Car> {

    private UUID uuid = AppConstantsTest.UUID_CAR;
    private String brandCar = AppConstantsTest.BRAND_CAR;
    private String model = AppConstantsTest.MODEL_CAR;
    private Enum<BodyType> bodyType = AppConstantsTest.BODY_TYPE_ENUM_CAR;
    private double engineCapacity = AppConstantsTest.ENGINE_CAPACITY;
    private Enum<Fuel> fuelType = AppConstantsTest.FUEL_ENUM_CAR;

    @Override
    public Car build() {
        final var car = new Car();
        car.setId(uuid);
        car.setBrand(brandCar);
        car.setModel(model);
        car.setBodyType(bodyType);
        car.setEngineCapacity(engineCapacity);
        car.setFuelType(fuelType);
        return car;
    }
}

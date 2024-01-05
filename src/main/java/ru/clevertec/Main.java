package ru.clevertec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.config.app.SpringConfig;
import ru.clevertec.config.db.LiquibaseStarter;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;
import ru.clevertec.exception.CarNotFoundException;
import ru.clevertec.exception.ValidationException;
import ru.clevertec.service.CarService;
import ru.clevertec.util.FileHandler;
import ru.clevertec.util.validation.Validation;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class Main {

    public static final String TABLE_NAME = "Table CarDTO from data base";
    public static final String PATH_FILE = "src/main/resources/list_cars";
    public static final String PDF = ".pdf";

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        context.getBean(LiquibaseStarter.class).createDbForStartProject();
        CarService service = context.getBean(CarService.class);

        CarDTO car1 = new CarDTO("Toyota", "Hilux", BodyType.PICKUP, 2.5, Fuel.DIESEL);
        CarDTO car2 = new CarDTO("BMW", "5 G30", BodyType.SEDAN, 3, Fuel.DIESEL);
        CarDTO car3 = new CarDTO("Volkswagen", "Passat B8", BodyType.STATION_WAGON, 2, Fuel.PETROL);
        CarDTO car4 = new CarDTO("BMW", "730", BodyType.SEDAN, 3.0, Fuel.PETROL);

        System.out.println(service.create(car1));
        System.out.println(service.create(car2));
        System.out.println(service.create(car3));
        System.out.println(service.create(car1));
        System.out.println(service.create(car2));
        System.out.println(service.create(car2));
        System.out.println(service.create(car4));
        service.delete(UUID.fromString("26635638-2f30-4891-81a0-bcac1f97b0d4"));
        try {
            service.get(UUID.fromString("26635638-2f30-4891-81a0-bcac1f97b0d4"));
        } catch (CarNotFoundException e) {
            log.error(e.getMessage());
        }

        try {
            System.out.println(service.get(UUID.fromString("1be4206e-787f-4708-924f-1adf9dd9a5da")));
        } catch (CarNotFoundException e) {
            log.error(e.getMessage());
        }

        System.out.println(service.update(UUID.fromString("1be4206e-787f-4708-924f-1adf9dd9a5da"), car4));

        CarDTO carNoValid = new CarDTO("BMW123", "730", BodyType.SEDAN, 3.0, Fuel.PETROL);
        try {
            Validation.validate(carNoValid);
        } catch (ValidationException e) {
            log.error(e.getMessage());
        }

        new FileHandler().writeTableToFilePDF(PATH_FILE + UUID.randomUUID() + PDF,
                service.getAll(),
                TABLE_NAME);
    }
}

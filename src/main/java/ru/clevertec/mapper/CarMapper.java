package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.entity.Car;

import java.util.List;

@Mapper
public interface CarMapper {

    /**
     * Maps CarDTO from Car without UUID
     *
     * @param car - Car object
     * @return new CarDTO
     */
    CarDTO toCarDto(Car car);

    /**
     * Maps the current DTO in Car
     *
     * @param carDto - CarDTO object
     * @return Car
     */
    @Mapping(target = "id", ignore = true)
    Car fromCarDto(CarDTO carDto);

    /**
     * Maps List<CarDTO> from List<Car> without UUID
     *
     * @param cars - List Car
     * @return new List CarDTO
     */
    List<CarDTO> toCardDtoList(List<Car> cars);
}

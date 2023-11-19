package ru.clevertec.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.dao.CarDAO;
import ru.clevertec.entity.Car;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.mapper.CarMapper;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;
import ru.clevertec.exception.CarNotFoundException;
import util.AppConstantsTest;
import util.CarTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    CarDAO carDAO;

    @Mock
    CarMapper carMapper;

    @InjectMocks
    CarServiceImpl carService;
    private final CarTest carTest = new CarTest();

    @Test
    void shouldThrowCarNotFoundException() {
        // given
        UUID uuid = UUID.fromString("0f39a05f-b9ea-46f6-b38a-f189052b63e8");

        // when,then
        assertThatThrownBy(() -> {
            carService.get(uuid);
        })
                .isInstanceOf(CarNotFoundException.class)
                .hasMessage("Car with uuid: 0f39a05f-b9ea-46f6-b38a-f189052b63e8 not found");
    }

    @Test
    void shouldGetCarDTOById() {
        // given
        CarDTO expected = new CarDTO(AppConstantsTest.BRAND_CAR, AppConstantsTest.MODEL_CAR, AppConstantsTest.BODY_TYPE_ENUM_CAR,
                AppConstantsTest.ENGINE_CAPACITY, AppConstantsTest.FUEL_ENUM_CAR);
        UUID uuid = AppConstantsTest.UUID_CAR;

        when(carMapper.toCarDto(carTest.build())).thenReturn(expected);
        when(carDAO.findById(AppConstantsTest.UUID_CAR)).thenReturn(Optional.of(carTest.build()));

        // when
        CarDTO actual = carService.get(uuid);

        // then
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void shouldSizeListTwo() {
        // given
        List<Car> carList = List.of(
                new Car(UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607"), "Toyota", "Hilux", BodyType.PICKUP, 2.5, Fuel.DIESEL),
                new Car(UUID.fromString("1be4206e-787f-4708-924f-1adf9dd9a5da"), "BMW", "5 G30", BodyType.SEDAN, 3, Fuel.DIESEL));

        List<CarDTO> expected = List.of(
                new CarDTO("Toyota", "Hilux", BodyType.PICKUP, 2.5, Fuel.DIESEL),
                new CarDTO("BMW", "5 G30", BodyType.SEDAN, 3, Fuel.DIESEL));

        when(carDAO.findAll()).thenReturn(carList);
        when(carMapper.toCardDtoList(carList)).thenReturn(expected);

        // when
        List<CarDTO> actual = carService.getAll();

        // then
        assertThat(actual).isNotNull()
                .hasSize(2)
                .isEqualTo(expected);
    }

    @Test
    void shouldActualUuidCarNotNullAndActualNotNull() {
        // given
        CarDTO dto = new CarDTO(AppConstantsTest.BRAND_CAR, AppConstantsTest.MODEL_CAR, AppConstantsTest.BODY_TYPE_ENUM_CAR,
                AppConstantsTest.ENGINE_CAPACITY, AppConstantsTest.FUEL_ENUM_CAR);

        when(carMapper.fromCarDto(dto)).thenReturn(carTest.build());

        // when
        carService.create(dto);

        // then
        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        verify(carDAO).save(carCaptor.capture());
        Car actual = carCaptor.getValue();

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void shouldActualCarEqualToExpectedCar() {
        // given
        CarDTO dto = new CarDTO(AppConstantsTest.BRAND_CAR, AppConstantsTest.MODEL_CAR, AppConstantsTest.BODY_TYPE_ENUM_CAR,
                AppConstantsTest.ENGINE_CAPACITY, AppConstantsTest.FUEL_ENUM_CAR);

        when(carDAO.save(carTest.build())).thenReturn(AppConstantsTest.UUID_CAR);
        when(carMapper.fromCarDto(dto)).thenReturn(carTest.build());

        // when
        UUID actual = carService.update(AppConstantsTest.UUID_CAR, dto);

        // then
        assertThat(actual).isEqualTo(AppConstantsTest.UUID_CAR);
    }

    @Test
    void shouldCallMethodDeleteToCarDao() {
        // given
        UUID uuid = AppConstantsTest.UUID_CAR;

        // when
        carService.delete(uuid);

        // then
        verify(carDAO, atLeastOnce()).delete(uuid);
    }
}
package ru.clevertec.service.proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cache.Cache;
import ru.clevertec.dao.impl.InPostgresCarDAO;
import ru.clevertec.entity.Car;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;
import util.AppConstantsTest;
import util.CarTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyCarDAOImplTest {

    @Mock
    InPostgresCarDAO carDAO;

    @Mock
    Cache<UUID, Car> cache;

    @InjectMocks
    ProxyCarDAOImpl proxyCarDAO;

    @Captor
    ArgumentCaptor<Car> carCaptor;
    private final CarTest carTest = new CarTest();

    @Test
    void shouldCallMethodSavaToCarDaoAndActualCarEqualExpected() {
        // given
        UUID expected = UUID.fromString("5cdd6de2-3dd1-4203-9089-7823b33d87fe");
        Car car = carTest
                .withUuid(expected)
                .build();

        when(carDAO.save(car)).thenReturn(expected);
        when(cache.put(expected, car)).thenReturn(car);

        // when
        UUID actual = proxyCarDAO.save(car);

        // then
        assertThat(actual).isEqualTo(expected);

        verify(carDAO, times(1)).save(carCaptor.capture());
        actual = carCaptor.getValue().getId();
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void shouldCallMethodRemoveByKeyToCacheAndCallMethodDeleteToCarDao() {
        // given
        UUID uuid = AppConstantsTest.UUID_CAR;

        // when
        proxyCarDAO.delete(uuid);

        // then
        verify(cache,atLeastOnce()).removeByKey(uuid);
        verify(carDAO,atLeastOnce()).delete(uuid);
    }

    @Test
    void shouldListActualEqualExpectedList() {
        List<Car> expected = List.of(
                new Car(UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607"), "Toyota", "Hilux", BodyType.PICKUP, 2.5, Fuel.DIESEL),
                new Car(UUID.fromString("1be4206e-787f-4708-924f-1adf9dd9a5da"), "BMW", "5 G30", BodyType.SEDAN, 3, Fuel.DIESEL));

        when(carDAO.findAll()).thenReturn(expected);

        // when
        List<Car> actual = proxyCarDAO.findAll();

        // then
        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void shouldCallMethodPutToCacheAndReturnCarFromCarDAO() {
        // given
        UUID uuid = AppConstantsTest.UUID_CAR;
        Optional<Car> expected = Optional.of(carTest.build());
        when(carDAO.findById(uuid)).thenReturn(expected);
        when(cache.get(uuid)).thenReturn(null);

        // when
        Optional<Car> actual = proxyCarDAO.findById(uuid);

        // then
        assertThat(actual)
                .isNotNull()
                .isPresent()
                .isEqualTo(expected);

        verify(cache).put(uuid, expected.get());
    }

    @Test
    void shouldCallMethodPutToCacheAndReturnCarFromCache() {
        // given
        UUID uuid = AppConstantsTest.UUID_CAR;
        Optional<Car> expected = Optional.of(carTest.build());

        when(cache.get(uuid)).thenReturn(expected.get());

        // when
        Optional<Car> actual = proxyCarDAO.findById(uuid);

        // then
        assertThat(actual)
                .isNotNull()
                .isPresent()
                .isEqualTo(expected);
        verify(cache).put(uuid, expected.get());
    }
}
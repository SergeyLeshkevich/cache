package ru.clevertec.cache;

import org.junit.jupiter.api.Test;
import ru.clevertec.cache.impl.LFUCache;
import ru.clevertec.entity.Car;
import util.CarTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LFUCacheTest {

    private final CarTest carTest = new CarTest();

    @Test
    void shouldActualIsNull() {
        // given
        Cache<UUID, Car> cache = new LFUCache<>(3);
        UUID carUUID = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");

        // when
        Car actual = cache.get(carUUID);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldActualEqualExpected() {
        // given
        Cache<UUID, Car> cache = new LFUCache<>(3);
        UUID carUUID = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");
        Car car = carTest.build();
        cache.put(carUUID, car);

        // when
        Car actual = cache.get(carUUID);

        // then
        assertThat(actual).isEqualTo(car);
    }

    @Test
    void shouldActualCacheToStringEqualExpectedString() {
        // given
        Cache<Integer, String> cache = new LFUCache<>(3);
        String expected = "LFUCache(listMap={1=[3], 2=[1], 3=[2]}, valueMap={1=a, 2=b, 3=c}," +
                " counterUseMap={1=2, 2=3, 3=1}, capacity=3)";

        // when
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(2, "b");
        cache.put(4, "e");
        cache.put(4, "e");
        cache.put(3, "c");
        String actual = cache.toString();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldActualCarEqualExpectedCar() {
        // given
        Cache<UUID, Car> cache = new LFUCache<>(3);
        UUID carUUID = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");
        Car car = carTest.build();
        cache.put(carUUID, car);

        // when
        Car actual = cache.removeByKey(carUUID);

        // then
        assertThat(actual).isEqualTo(car);
    }
}
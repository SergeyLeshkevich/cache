package ru.clevertec.cache;

import org.junit.jupiter.api.Test;
import ru.clevertec.cache.impl.LRUCache;
import ru.clevertec.entity.Car;
import util.CarTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LRUCacheTest {

    private final CarTest carTest = new CarTest();

    @Test
    void shouldActualIsNull() {
        // given
        Cache<UUID, Car> cache = new LRUCache<>(3);
        UUID carUUID = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");

        // when
        Car actual = cache.get(carUUID);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldActualEqualExpected() {

        // given
        Cache<UUID, Car> cache = new LRUCache<>(3);
        UUID carUUID = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");
        Car car = carTest.build();
        cache.put(carUUID,car);

        // when
        Car actual = cache.get(carUUID);

        // then
        assertThat(actual).isEqualTo(car);
    }

    @Test
    void shouldActualCacheToStringEqualExpectedString() {
        // given
        Cache<Integer, String> cache = new LRUCache<>(3);
        String expected = "LRUCache(list=[3, 4, 1], valueMap={1=a, 3=c, 4=e}, capacity=3)";

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
        Cache<UUID, Car> cache = new LRUCache<>(3);
        UUID carUUID = UUID.fromString("44762276-3651-43ce-a4b3-e7587acbc607");
        Car expected = carTest.build();
        cache.put(carUUID,expected);

        // when
        Car actual = cache.removeByKey(carUUID);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
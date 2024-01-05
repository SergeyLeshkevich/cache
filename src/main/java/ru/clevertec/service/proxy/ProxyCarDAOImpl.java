package ru.clevertec.service.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.cache.Cache;
import ru.clevertec.dao.CarDAO;
import ru.clevertec.entity.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
public class ProxyCarDAOImpl implements ProxyCarDAO {

    private final CarDAO dao;
    private final Cache<UUID, Car> cache;

    /**
     * Saves new car via DAO and adds it to cache
     *
     * @param car car
     * @return identifier of the saved car
     */
    @Override
    public UUID save(Car car) {
        UUID uuidCar = car.getId();
        log.info("Метод save() ProxyCarImpl.Объект Car {} сохраненение/обновление в БД", car);
        dao.save(car);
        log.info("Метод save() ProxyCarImpl.Объект Car {} сохраненение/обновление в кеш", car);
        return cache.put(uuidCar, car).getId();
    }

    /**
     * Removes existing car via DAO and from cache
     *
     * @param uuid car identifier to delete
     */
    @Override
    public void delete(UUID uuid) {
        log.info("Метод delete() ProxyCarImpl.Объект Car с uuid {} удаление из БД", uuid);
        dao.delete(uuid);
        log.info("Метод delete() ProxyCarImpl.Объект Car с uuid {} удаление из кеша", uuid);
        cache.removeByKey(uuid);
    }

    /**
     * Returns all existing cars
     *
     * @return a list with information about cars
     */
    @Override
    public List<Car> findAll() {
        log.info("Метод findAll() ProxyCarImpl.Получение List<Car> из БД");
        return dao.findAll();
    }

    /**
     * Searches for a car in the cache by ID, if it doesn’t find it, then searches it in DAO
     *
     * @param uuid vehicle identifier
     * @return the found car. If not found, returns null
     */
    @Override
    public Optional<Car> findById(UUID uuid) {
        log.info("Метод findById() ProxyCarImpl.Получение объекта Car с uuid {} из кеша", uuid);
        Car car = cache.get(uuid);
        if (car == null) {
            log.info("Метод findById() ProxyCarImpl.Объект Car с uuid {} из кеша равен null", uuid);
            log.info("Метод findById() ProxyCarImpl.Получение объекта Car с uuid {} из БД", uuid);
            Optional<Car> optional = dao.findById(uuid);
            if (optional.isPresent()) {
                car = optional.get();
                log.info("Метод findById() ProxyCarImpl.Обновление объекта Car с uuid {} в кеше", uuid);
                cache.put(car.getId(), car);
                return Optional.of(car);
            }
            return optional;
        }
        log.info("Метод findById() ProxyCarImpl.Обновление объекта Car с uuid {} в кеше", uuid);
        cache.put(car.getId(), car);
        return Optional.of(car);
    }

    @Override
    public List<Car> findLimitList(int limit, int numberStartSelection) {
        log.info("Метод findLimitList(int limit, int numberStartSelection) ProxyCarImpl.Получение  LimitList<Car> из БД");
        return dao.findLimitList(limit,numberStartSelection);
    }
}

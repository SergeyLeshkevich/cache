package ru.clevertec.service.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.impl.LFUCache;
import ru.clevertec.dao.CarDAO;
import ru.clevertec.entity.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class ProxyCarDAOImpl implements ProxyCarDAO {

    private static final Logger logger = LogManager.getLogger(LFUCache.class);
    private final CarDAO dao;
    private final Cache<UUID, Car> cache;

    public ProxyCarDAOImpl(CarDAO dao, Cache<UUID, Car> cache) {
        this.cache = cache;
        this.dao = dao;
    }

    /**
     * Saves new car via DAO and adds it to cache
     *
     * @param car car
     * @return identifier of the saved car
     */
    @Override
    public UUID save(Car car) {
        UUID uuidCar = car.getId();
        logger.info("Метод save() ProxyCarImpl.Объект Car {} сохраненение/обновление в БД", car);
        dao.save(car);
        logger.info("Метод save() ProxyCarImpl.Объект Car {} сохраненение/обновление в кеш", car);
        return cache.put(uuidCar, car).getId();
    }

    /**
     * Removes existing car via DAO and from cache
     *
     * @param uuid car identifier to delete
     */
    @Override
    public void delete(UUID uuid) {
        logger.info("Метод delete() ProxyCarImpl.Объект Car с uuid {} удаление из БД", uuid);
        dao.delete(uuid);
        logger.info("Метод delete() ProxyCarImpl.Объект Car с uuid {} удаление из кеша", uuid);
        cache.removeByKey(uuid);
    }

    /**
     * Returns all existing cars
     *
     * @return a list with information about cars
     */
    @Override
    public List<Car> findAll() {
        logger.info("Метод findAll() ProxyCarImpl.Получение List<Car> из БД");
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
        logger.info("Метод findById() ProxyCarImpl.Получение объекта Car с uuid {} из кеша", uuid);
        Car car = cache.get(uuid);
        if (car == null) {
            logger.info("Метод findById() ProxyCarImpl.Объект Car с uuid {} из кеша равен null", uuid);
            logger.info("Метод findById() ProxyCarImpl.Получение объекта Car с uuid {} из БД", uuid);
            Optional<Car> optional = dao.findById(uuid);
            if (optional.isPresent()) {
                car = optional.get();
                logger.info("Метод findById() ProxyCarImpl.Обновление объекта Car с uuid {} в кеше", uuid);
                cache.put(car.getId(), car);
                return Optional.of(car);
            }
            return optional;
        }
        logger.info("Метод findById() ProxyCarImpl.Обновление объекта Car с uuid {} в кеше", uuid);
        cache.put(car.getId(), car);
        return Optional.of(car);
    }
}

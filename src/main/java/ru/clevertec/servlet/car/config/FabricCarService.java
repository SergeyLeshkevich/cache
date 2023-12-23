package ru.clevertec.servlet.car.config;

import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.cache.impl.CacheFactoryImpl;
import ru.clevertec.config.db.DatabaseConfig;
import ru.clevertec.dao.impl.InPostgresCarDAO;
import ru.clevertec.entity.Car;
import ru.clevertec.mapper.CarMapper;
import ru.clevertec.service.CarService;
import ru.clevertec.service.impl.CarServiceImpl;
import ru.clevertec.service.proxy.ProxyCarDAOImpl;

import javax.sql.DataSource;
import java.util.UUID;

public final class FabricCarService {

    private static FabricCarService instance;
    private final CarService carService;

    private FabricCarService() {
        DataSource dataSource = DatabaseConfig.dataSource();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        CacheFactory<UUID, Car> cacheFactory = new CacheFactoryImpl<>();
        ProxyCarDAOImpl carDAO = new ProxyCarDAOImpl(new InPostgresCarDAO(template), cacheFactory.createCache());
        CarMapper carMapper = Mappers.getMapper(CarMapper.class);
        this.carService = new CarServiceImpl(carDAO, carMapper);
    }

    public static synchronized FabricCarService getInstance() {
        if (instance == null) {
            instance = new FabricCarService();
        }
        return instance;
    }

    public CarService getCarService() {
        return carService;
    }
}

package ru.clevertec.config.app;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.dao.CarDAO;
import ru.clevertec.dao.impl.InPostgresCarDAO;
import ru.clevertec.entity.Car;
import ru.clevertec.service.proxy.ProxyCarDAOImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@ComponentScan("ru.clevertec")
@Configuration
@PropertySource("classpath:application.yaml")
public class SpringConfig {

    private static final String USER_KEY = "user";
    private static final String DRIVER_KEY = "driver";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";
    private static final String CHANGELOG_FILE = "db/changelog.xml";

    private final Environment environment;

    @Autowired
    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty(DRIVER_KEY)));
        dataSource.setUrl(environment.getProperty(URL_KEY));
        dataSource.setUsername(environment.getProperty(USER_KEY));
        dataSource.setPassword(environment.getProperty(PASSWORD_KEY));

        return dataSource;
    }

    @Bean
    public Connection connection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public CarDAO proxyCarDaoImpl(CacheFactory<UUID, Car> cacheFactory, InPostgresCarDAO carDAO) {
        Cache<UUID, Car> cache = cacheFactory.createCache();
        return new ProxyCarDAOImpl(carDAO, cache);
    }

    @Bean
    public Database getDatabase(Connection connection) {
        try {
            return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Liquibase liquibase(Database database) {
        return new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database);
    }

    @Bean
    public Contexts contexts() {
        return new Contexts();
    }

    @Bean
    public LabelExpression getLabelExpression() {
        return new LabelExpression();
    }
}

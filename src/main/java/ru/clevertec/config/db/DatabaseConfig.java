package ru.clevertec.config.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.util.YmlManager;

import java.io.FileNotFoundException;
import java.util.Map;

public class DatabaseConfig {

    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class);
    private static final String FILENAME = "src/main/resources/application.yaml";
    private static final String USERNAME_KEY = "username";
    private static final String DRIVER_KEY = "driver";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";

    public static BasicDataSource dataSource() {
        Map<String, Object> configValueMap = null;
        BasicDataSource dataSource = new BasicDataSource();
        try {
            configValueMap = YmlManager.getValue(FILENAME);
            dataSource.setDriverClassName((String) configValueMap.get(DRIVER_KEY));
            dataSource.setUrl((String) configValueMap.get(URL_KEY));
            dataSource.setUsername((String) configValueMap.get(USERNAME_KEY));
            dataSource.setPassword((String) configValueMap.get(PASSWORD_KEY));
        } catch (FileNotFoundException e) {
            logger.error(e);
        }

        return dataSource;
    }
}

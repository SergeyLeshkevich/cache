package ru.clevertec.servlet.car.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.config.db.LiquibaseStarter;
import ru.clevertec.util.YamlManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@WebListener
public class ServletListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ServletListener.class);

    public static final String APPLICATION_YAML = "\\application.yaml";
    private static final String USERNAME_KEY = "username";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";
    public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Map<String, Object> configValueMap;

        YamlManager yamlManager = new YamlManager();
        configValueMap = yamlManager.getValue(APPLICATION_YAML);
        Connection connection = null;
        try {
            Class.forName(ORG_POSTGRESQL_DRIVER);
            connection = DriverManager.getConnection((String) configValueMap.get(URL_KEY),
                    (String) configValueMap.get(USERNAME_KEY),
                    (String) configValueMap.get(PASSWORD_KEY));
            new LiquibaseStarter().createDbForStartProject(connection);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
        } finally {
            try {
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }
}

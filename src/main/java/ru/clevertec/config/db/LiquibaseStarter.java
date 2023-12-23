package ru.clevertec.config.db;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.util.YamlManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
public class LiquibaseStarter {

    public static final String APPLICATION_YAML = "\\application.yaml";
    private static final String USERNAME_KEY = "username";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";
    private static final String CHANGELOG_FILE = "db/changelog.xml";
    private final YamlManager yamlManager;

    public LiquibaseStarter() {
        this.yamlManager = new YamlManager();
    }

    public void createDbForStartProject() {
        Map<String, Object> configValueMap;
        try {
            configValueMap = yamlManager.getValue(APPLICATION_YAML);
            try (Connection connection = DriverManager.getConnection((String) configValueMap.get(URL_KEY),
                    (String) configValueMap.get(USERNAME_KEY),
                    (String) configValueMap.get(PASSWORD_KEY))) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database);
                Contexts contexts = new Contexts();
                LabelExpression labelExpression = new LabelExpression();
                liquibase.update(contexts, labelExpression);
            } catch (LiquibaseException e) {
                log.error(e.getMessage());
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public void createDbForStartProject(Connection connection) {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database);
            Contexts contexts = new Contexts();
            LabelExpression labelExpression = new LabelExpression();
            liquibase.update(contexts, labelExpression);
        } catch (LiquibaseException e) {
            log.error(e.getMessage());
        }
    }
}

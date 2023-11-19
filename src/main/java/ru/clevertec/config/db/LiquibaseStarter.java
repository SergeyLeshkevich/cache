package ru.clevertec.config.db;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.clevertec.util.YmlManager;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class LiquibaseStarter {

    private static final String FILENAME = "src/main/resources/application.yaml";
    private static final String USERNAME_KEY = "username";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";
    private static final String CHANGELOG_FILE = "db/changelog.xml";

    public static void creatDbForStartProject() {
        Map<String, Object> configValueMap = null;
        try {
            configValueMap = YmlManager.getValue(FILENAME);

            try (Connection connection = DriverManager.getConnection((String) configValueMap.get(URL_KEY),
                    (String) configValueMap.get(USERNAME_KEY),
                    (String) configValueMap.get(PASSWORD_KEY))) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database);
                Contexts contexts = new Contexts();
                LabelExpression labelExpression = new LabelExpression();
                liquibase.update(contexts, labelExpression);
            } catch (LiquibaseException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

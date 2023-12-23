package ru.clevertec.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.clevertec.util.YamlManager;

import java.util.Map;

public class DatabaseConfig {

    public static final String APPLICATION_YAML = "\\application.yaml";
    private static final String USERNAME_KEY = "username";
    private static final String DRIVER_KEY = "driver";
    private static final String URL_KEY = "url";
    private static final String PASSWORD_KEY = "password";
    private static final String CACHE_PREP_STMTS = "cachePrepStmts";
    private static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    private static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        Map<String, Object> configValueMap = null;
        YamlManager yamlManager = new YamlManager();
        configValueMap = yamlManager.getValue(APPLICATION_YAML);
        config.setJdbcUrl((String) configValueMap.get(URL_KEY));
        config.setUsername((String) configValueMap.get(USERNAME_KEY));
        config.setPassword((String) configValueMap.get(PASSWORD_KEY));
        config.setDriverClassName((String) configValueMap.get(DRIVER_KEY));
        config.addDataSourceProperty(CACHE_PREP_STMTS, configValueMap.get(CACHE_PREP_STMTS));
        config.addDataSourceProperty(PREP_STMT_CACHE_SIZE, configValueMap.get(PREP_STMT_CACHE_SIZE));
        config.addDataSourceProperty(PREP_STMT_CACHE_SQL_LIMIT, configValueMap.get(PREP_STMT_CACHE_SQL_LIMIT));
        ds = new HikariDataSource(config);
    }

    public static HikariDataSource dataSource() {
        return ds;
    }
}

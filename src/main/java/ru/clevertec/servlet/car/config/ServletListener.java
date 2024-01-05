package ru.clevertec.servlet.car.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.config.app.SpringConfig;
import ru.clevertec.config.db.LiquibaseStarter;

@WebListener
@Slf4j
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        LiquibaseStarter liquibaseStarter = context.getBean(LiquibaseStarter.class);
        liquibaseStarter.createDbForStartProject();
    }
}

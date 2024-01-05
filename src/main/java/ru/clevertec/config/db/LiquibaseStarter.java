package ru.clevertec.config.db;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiquibaseStarter {

    private final Contexts contexts;
    private final LabelExpression labelExpression;
    private final Liquibase liquibase;

    public void createDbForStartProject() {
        try {
            liquibase.update(contexts, labelExpression);
        } catch (LiquibaseException e) {
            log.error(e.getMessage());
        }
    }
}

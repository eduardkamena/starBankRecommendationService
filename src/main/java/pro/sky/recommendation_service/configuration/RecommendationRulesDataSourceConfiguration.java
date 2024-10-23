package pro.sky.recommendation_service.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class RecommendationRulesDataSourceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationRulesDataSourceConfiguration.class);

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSourceProperties secondaryDataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        logger.info("Second(Rules) DataSource Properties: {}", properties);
        return properties;
    }

    @Bean(name = "rulesDataSource")
    public DataSource secondaryDataSource() {
        DataSource dataSource = secondaryDataSourceProperties().initializeDataSourceBuilder().build();
        logger.info("Second(Rules) DataSource: {}", dataSource);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionalManager(
            @Qualifier("rulesDataSource") DataSource secondaryDataSource) {
        return new DataSourceTransactionManager(secondaryDataSource);
    }

   @Bean(name = "rulesJdbcTemplate")
    public JdbcTemplate rulesJdbcTemplate(
            @Qualifier("rulesDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary.liquibase")
    public LiquibaseProperties secondaryLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase secondaryLiquibase(){
        return secondaryLiquibase(secondaryDataSource(), secondaryLiquibaseProperties());
    }

    public static SpringLiquibase secondaryLiquibase(
            DataSource dataSource, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        return liquibase;
    }
}

package pro.sky.recommendation_service.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class RecommendationsDataSourceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationsDataSourceConfiguration.class);
    // Добавление источника данных — DataSource
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        logger.info("First(Transactions) DataSource Properties: {}", properties);
        return properties;
    }

    @Primary
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource(){
        DataSource dataSource = primaryDataSourceProperties().initializeDataSourceBuilder().build();
        logger.info("First(Transactions) DataSource: {}", dataSource);
        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager firstTransactionalManager(
            @Qualifier("recommendationsDataSource") DataSource primaryDataSource) {
        return new DataSourceTransactionManager(primaryDataSource);
    }

    // Добавление конфигурации для JdbcTemplate
    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}

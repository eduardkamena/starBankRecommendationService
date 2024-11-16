package pro.sky.recommendation_service.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Конфигурация источников баз данных
 */
@Configuration
public class DataSourceConfig {

    /**
     * Конфигурирование H2-Базы данных
     *
     * @param recommendationsUrl путь до БД указанный в application.properties
     * @return настроенный для работы с H2 объект {@link DataSource}
     */
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource(
            @Value("${application.recommendations-db.url}") String recommendationsUrl) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(recommendationsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    /**
     * Создание подготовленного JDBC Template для работы с БД
     *
     * @param dataSource сконфигурированный источник БД
     *                   см.{@link DataSourceConfig#recommendationsDataSource recommendationsDataSource}
     * @return JdbcTemplate с конфигурацией DataSource
     */
    @Bean(name = "recommendationsJdbcTemplate")
    @Qualifier("recommendationsDataSource")
    public JdbcTemplate recommendationsJdbcTemplate(@Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Конфигурирование PostgreSQL-Базы данных
     *
     * @param properties конфигурация полученная из application.properties
     *                   согласно prefix
     * @return DataSource PostgreSQL-БД
     */
    @Primary
    @Bean(name = "postgresDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource defaultDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

}

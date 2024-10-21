package pro.sky.recommendation_service.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RecommendationsDataSourceConfiguration {

    // Добавление источника данных — DataSource
    @Primary
    @Bean(name = "recommendationsDataSource")
    @Value("${application.recommendations-db.url}")
    public DataSource recommendationsDataSource(String recommendationsUrl) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(recommendationsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    // Добавление конфигурации для JdbcTemplate
    @Bean(name = "recommendationsJdbcTemplate")
    @Qualifier("recommendationsDataSource")
    public JdbcTemplate recommendationsJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}

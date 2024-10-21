package pro.sky.recommendation_service.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RecommendationRulesDataSourceConfiguration {

    @Bean(name = "rulesDataSource")
    public DataSource rulesDataSource(
            @Value("${application.recommendation-rules-db.url}") String rulesUrl) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(rulesUrl);
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setReadOnly(false);
        return dataSource;
    }

    @Bean(name = "rulesJdbcTemplate")
    public JdbcTemplate rulesJdbcTemplate(
            @Qualifier("rulesDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

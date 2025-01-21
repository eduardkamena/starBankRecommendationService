package pro.sky.recommendation_service.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@TestConfiguration
public class JDBCTestConfig {

    @Bean
    @Qualifier("recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}

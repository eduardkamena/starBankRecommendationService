package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;

import java.sql.Types;
import java.util.UUID;

@Repository
public class DynamicProductRecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public DynamicProductRecommendationsRepository(
            @Qualifier("dynamicsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DynamicProductRecommendation createProductRecommendation(DynamicProductRecommendation recommendation) {
        String sql = "INSERT INTO product_recommendations (recommendation_id, product_id, product_name, product_description, recommendationRules)" +
                " VALUES (?, ?, ?, ?, ?)";
        UUID id = UUID.randomUUID();

        jdbcTemplate.update(sql);
        return recommendation;
    }
}
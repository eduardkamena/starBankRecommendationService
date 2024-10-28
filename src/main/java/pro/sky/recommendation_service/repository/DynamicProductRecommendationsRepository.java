package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.dto.DynamicRecommendationDTO;

import java.util.UUID;

@Repository
public class DynamicProductRecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public DynamicProductRecommendationsRepository(
            @Qualifier("dynamicsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID createProductRecommendation(DynamicRecommendationDTO recommendation) {
        String sql = "INSERT INTO product_recommendations " +
                "(recommendation_id, product_id, product_name, product_description)" +
                " VALUES (?, ?, ?, ?)";
        final UUID ID = UUID.randomUUID();

        jdbcTemplate.update(sql,
                ID,
                recommendation.getProduct_id(),
                recommendation.getProduct_name(),
                recommendation.getProduct_text());
        return ID;
    }
}
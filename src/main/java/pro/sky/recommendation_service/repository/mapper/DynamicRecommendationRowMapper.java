package pro.sky.recommendation_service.repository.mapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;
import pro.sky.recommendation_service.entity.RecommendationRule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DynamicRecommendationRowMapper implements RowMapper<DynamicProductRecommendation> {
    private final JdbcTemplate jdbcTemplate;
    private final RuleRowMapper ruleRowMapper;

    public DynamicRecommendationRowMapper(@Qualifier("dynamicsJdbcTemplate") JdbcTemplate jdbcTemplate, RuleRowMapper ruleRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.ruleRowMapper = ruleRowMapper;
    }

    private final Map<UUID, DynamicProductRecommendation> recommendationsMap = new HashMap<>();

    public DynamicProductRecommendation mapRow(ResultSet rs, int rowNum) throws SQLException {

        UUID recommendationId = rs.getObject("recommendation_id", UUID.class);
        DynamicProductRecommendation recommendation = recommendationsMap.get(recommendationId);

        if (recommendation == null) {
            recommendation = new DynamicProductRecommendation();
            recommendation.setId(recommendationId);
            recommendation.setProductName(rs.getString("product_name"));
            recommendation.setProductId(rs.getObject("product_id", UUID.class));
            recommendation.setProductDescription(rs.getString("product_description"));
            recommendationsMap.put(recommendationId, recommendation);
        }

        String rulesSql = "select * from product_recommendations where recommendation_id=?";
        List<RecommendationRule> rules = jdbcTemplate.query(rulesSql, ruleRowMapper, recommendationId);
        recommendation.setRecommendationRules(rules);

        return recommendation;
    }
}

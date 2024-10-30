package pro.sky.recommendation_service.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;
import pro.sky.recommendation_service.entity.RecommendationRule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DynamicRecommendationRowMapper implements RowMapper<DynamicProductRecommendation> {
    private final RuleRowMapper ruleRowMapper;

    public DynamicRecommendationRowMapper(RuleRowMapper ruleRowMapper) {
        this.ruleRowMapper = ruleRowMapper;
    }

    private final Map<UUID, DynamicProductRecommendation> recommendationsMap = new HashMap<>();
    private final Map<UUID, List<RecommendationRule>> rulesMap = new HashMap<>();

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

            List<RecommendationRule> rules = ruleRowMapper.getRulesForRecommendation(recommendationId);
            rulesMap.put(recommendationId, rules);
        }

        recommendation.setRecommendationRules(rulesMap.get(recommendationId));

        return recommendation;
    }

    public List<DynamicProductRecommendation> getAllRecommendations() {
        return new ArrayList<>(recommendationsMap.values());
    }
}

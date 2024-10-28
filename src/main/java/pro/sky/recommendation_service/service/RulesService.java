package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RecommendationRuleDTO;
import pro.sky.recommendation_service.entity.RecommendationRule;

import java.util.List;
import java.util.UUID;

public interface RulesService {

    List<RecommendationRule> getAllRules();
    void createSimpleRule(RecommendationRuleDTO rule);
    void createRuleFromRecommendation(UUID id, RecommendationRuleDTO rule);
//    void deleteRule(UUID id);

}

package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DynamicRulesRecommendationsService {

    Recommendations createDynamicRuleRecommendation(RecommendationsDTO recommendationsDTO);

    Optional<RecommendationsDTO> getDynamicRuleRecommendation(UUID id);

    List<RecommendationsDTO> getAllDynamicRulesRecommendations();

    void deleteDynamicRuleRecommendation(UUID id);

}

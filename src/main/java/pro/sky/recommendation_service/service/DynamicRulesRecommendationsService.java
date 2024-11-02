package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DynamicRulesRecommendationsService {

    Recommendations createRecommendation(RecommendationsDTO recommendationsDTO);

    Optional<RecommendationsDTO> getRule(UUID id);

    void deleteRule(UUID id);

    List<RecommendationsDTO> getAllRules();

}

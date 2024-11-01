package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationsService {

    Recommendations createRecommendation(RecommendationsDTO recommendationsDTO);

    Optional<RecommendationsDTO> readRule(UUID id);

    void deleteRule(UUID id);

}

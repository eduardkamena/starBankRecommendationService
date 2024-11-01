package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.UUID;

public interface RecommendationsService {

    Recommendations createRecommendation(RecommendationsDTO recommendationsDTO);

    Recommendations readRule(UUID id);

    void deleteRule(UUID id);

}

package pro.sky.recommendation_service.service;

import java.util.UUID;

public interface RecommendationService {

    int getRandomTransactionAmount(UUID user);

}

package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.UserRecommendationsDTO;

import java.util.UUID;

public interface UserFixedRecommendationsService {

    UserRecommendationsDTO getAllRecommendations(UUID user_id);

}

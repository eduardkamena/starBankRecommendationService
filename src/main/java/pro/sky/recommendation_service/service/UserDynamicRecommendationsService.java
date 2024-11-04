package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;

import java.util.List;
import java.util.UUID;

public interface UserDynamicRecommendationsService {

    UserRecommendationsDTO getAllDynamicRecommendations(UUID user_id);

}

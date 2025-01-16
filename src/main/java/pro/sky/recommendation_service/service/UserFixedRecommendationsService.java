package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.UserRecommendationsDTO;

import java.util.UUID;

/**
 * Интерфейс для работы с фиксированными рекомендациями для пользователей.
 * <p>
 * Определяет метод для получения всех фиксированных рекомендаций для пользователя.
 */
public interface UserFixedRecommendationsService {

    /**
     * Получение всех фиксированных рекомендаций для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return объект DTO с рекомендациями для пользователя
     */
    UserRecommendationsDTO getAllFixedRecommendations(UUID userId);

}

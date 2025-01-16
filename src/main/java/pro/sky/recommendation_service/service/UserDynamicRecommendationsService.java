package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.UserRecommendationsDTO;

import java.util.UUID;

/**
 * Интерфейс для работы с динамическими рекомендациями для пользователей.
 * <p>
 * Определяет методы для получения всех динамических рекомендаций для пользователя.
 */
public interface UserDynamicRecommendationsService {

    /**
     * Получение всех динамических рекомендаций для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return объект DTO с рекомендациями для пользователя
     */
    UserRecommendationsDTO getAllDynamicRecommendations(UUID userId);

    /**
     * Получение всех динамических рекомендаций для пользователя в формате строки для Telegram.
     *
     * @param userId идентификатор пользователя
     * @return строка с рекомендациями для пользователя
     */
    String getAllDynamicRulesRecommendationsForTelegramBot(UUID userId);

}

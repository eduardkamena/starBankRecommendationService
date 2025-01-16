package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для работы с динамическими рекомендациями.
 * <p>
 * Определяет методы для создания, получения, удаления и получения всех динамических рекомендаций.
 */
public interface DynamicRulesRecommendationsService {

    /**
     * Создание динамической рекомендации.
     *
     * @param recommendationsDTO объект DTO с данными для создания рекомендации
     * @return созданная рекомендация
     */
    Recommendations createDynamicRuleRecommendation(RecommendationsDTO recommendationsDTO);

    /**
     * Получение динамической рекомендации по ID.
     *
     * @param id идентификатор рекомендации
     * @return объект DTO с данными рекомендации
     */
    Optional<RecommendationsDTO> getDynamicRuleRecommendation(UUID id);

    /**
     * Получение всех динамических рекомендаций.
     *
     * @return список всех динамических рекомендаций
     */
    List<RecommendationsDTO> getAllDynamicRulesRecommendations();

    /**
     * Удаление динамической рекомендации по ID.
     *
     * @param id идентификатор рекомендации
     */
    void deleteDynamicRuleRecommendation(UUID id);

}

package pro.sky.recommendation_service.component;

import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для реализации фиксированных правил рекомендаций.
 * <p>
 * Каждое правило должно реализовывать метод {@link #checkRecommendation(UUID)},
 * который проверяет выполнение условий для предоставления рекомендации.
 */
public interface FixedRecommendationsRulesSet {

    /**
     * Проверка выполнения условий для предоставления рекомендации.
     *
     * @param userId идентификатор пользователя
     * @return список рекомендаций, если условия выполнены, иначе пустой Optional
     */
    Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID userId);

}

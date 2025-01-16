package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.recommendation_service.entity.Rules;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с правилами рекомендаций через JPA.
 * <p>
 * Используется для выполнения операций CRUD с сущностями правил рекомендаций.
 */
public interface RulesRecommendationsRepository extends JpaRepository<Rules, UUID> {

    /**
     * Поиск правил рекомендации по идентификатору рекомендации.
     *
     * @param recommendationId идентификатор рекомендации
     * @return список правил рекомендации
     */
    List<Rules> findByRecommendationsId(UUID recommendationId);

}

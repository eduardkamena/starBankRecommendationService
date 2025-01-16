package pro.sky.recommendation_service.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с динамическими рекомендациями через JPA.
 * <p>
 * Используется для выполнения операций CRUD с сущностями рекомендаций.
 */
public interface DynamicJPARecommendationsRepository extends JpaRepository<Recommendations, UUID> {

    /**
     * Получение всех идентификаторов рекомендаций.
     *
     * @return список идентификаторов рекомендаций
     */
    @Query(value = "SELECT r.id AS recommendations_id FROM recommendations r ", nativeQuery = true)
    List<UUID> findAllRecommendationsIDs();

    /**
     * Проверка существования рекомендации по идентификатору.
     *
     * @param id идентификатор рекомендации
     * @return true, если рекомендация существует, иначе false
     */
    boolean existsById(@NotNull UUID id);

    /**
     * Получение статистики срабатывания рекомендаций.
     *
     * @return список статистики срабатывания рекомендаций
     */
    @Query(value = "SELECT s.count, s.recommendations_id " +
            "           FROM stats s " +
            "               INNER JOIN recommendations r " +
            "               ON s.recommendations_id = r.id", nativeQuery = true)
    List<Object[]> findAllStats();

}

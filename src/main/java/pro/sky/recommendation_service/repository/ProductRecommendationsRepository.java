package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с продуктами рекомендаций через JPA.
 * <p>
 * Используется для выполнения операций CRUD с сущностями продуктов рекомендаций.
 */
public interface ProductRecommendationsRepository extends JpaRepository<Recommendations, UUID> {

    /**
     * Поиск продукта рекомендации по идентификатору.
     *
     * @param productId идентификатор продукта
     * @return список данных о продукте рекомендации
     */
    @Query(value = "SELECT r.product_name, r.product_id, r.product_text " +
            "           FROM recommendations r " +
            "           WHERE r.product_id = :productId", nativeQuery = true)
    List<Object[]> findByProductId(@Param("productId") UUID productId);

    /**
     * Проверка существования продукта рекомендации по идентификатору.
     *
     * @param productId идентификатор продукта
     * @return true, если продукт существует, иначе false
     */
    @Query(value = "SELECT " +
            "           CASE " +
            "               WHEN COUNT(r) > 0 " +
            "               THEN true " +
            "               ELSE false " +
            "           END " +
            "       FROM recommendations r " +
            "       WHERE r.product_id = :productId", nativeQuery = true)
    boolean existsByProductId(@Param("productId") UUID productId);

}

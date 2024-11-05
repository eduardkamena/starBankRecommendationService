package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.UUID;

public interface ProductRecommendationsRepository extends JpaRepository<Recommendations, UUID> {

    @Query(value = "SELECT r.product_name, r.product_id, r.product_text " +
            "           FROM recommendations r " +
            "           WHERE r.product_id = :product_id", nativeQuery = true)
    List<Object[]> findByProductId(@Param("product_id") UUID product_id);

    @Query(value = "SELECT " +
            "           CASE " +
            "               WHEN COUNT(r) > 0 " +
            "               THEN true " +
            "               ELSE false " +
            "           END " +
            "       FROM recommendations r " +
            "       WHERE r.product_id = :product_id", nativeQuery = true)
    boolean existsByProductId(UUID product_id);

}

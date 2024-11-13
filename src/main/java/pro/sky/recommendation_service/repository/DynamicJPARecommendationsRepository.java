package pro.sky.recommendation_service.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.List;
import java.util.UUID;

public interface DynamicJPARecommendationsRepository extends JpaRepository<Recommendations, UUID> {

    @Query(value = "SELECT r.id AS recommendations_id FROM recommendations r ", nativeQuery = true)
    List<UUID> findAllRecommendationsIDs();

    boolean existsById(@NotNull UUID id);

    @Query(value = "SELECT s.count, s.recommendations_id " +
            "           FROM stats s " +
            "               INNER JOIN recommendations r " +
            "               ON s.recommendations_id = r.id", nativeQuery = true)
    List<Object[]> findAllStats();

}

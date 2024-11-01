package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.recommendation_service.entity.Recommendations;

import java.util.UUID;

public interface RecommendationsRepository extends JpaRepository<Recommendations, UUID> {

}

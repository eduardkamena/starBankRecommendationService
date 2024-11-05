package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.recommendation_service.entity.Rules;

import java.util.List;
import java.util.UUID;

public interface RulesRecommendationsRepository extends JpaRepository<Rules, UUID> {

    List<Rules> findByRecommendationsId(UUID recommendation_id);

}

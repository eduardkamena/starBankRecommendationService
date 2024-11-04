package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.recommendation_service.entity.Rules;

import java.util.List;
import java.util.UUID;

public interface RulesRecommendationsRepository extends JpaRepository<Rules, UUID> {

//    @Query(value = "SELECT ra.argument " +
//            "FROM rules ru INNER JOIN rules_arguments ra ON ru.id = ra.rules_id " +
//            "INNER JOIN public.recommendations r on r.id = ru.recommendations_id " +
//            "WHERE ru.recommendations_id = :recommendation_id", nativeQuery = true)
//    List<Rules> findByRecommendationId(UUID recommendation_id);

       // @Query(value = "SELECT * from rules r", nativeQuery = true)
    List<Rules> findByRecommendationsId(UUID recommendation_id);

}

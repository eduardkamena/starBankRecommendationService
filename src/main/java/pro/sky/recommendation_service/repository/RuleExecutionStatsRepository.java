package pro.sky.recommendation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.recommendation_service.entity.RuleExecutionStats;

import java.util.Optional;

public interface RuleExecutionStatsRepository extends JpaRepository<RuleExecutionStats, Long> {
    Optional<RuleExecutionStats> findByRuleId(Long ruleId);
}

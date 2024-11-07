package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.entity.RuleExecutionStats;

import java.util.List;

public interface RuleExecutionStatsService {
    void incrementRuleExecutionCount(Long ruleId);
    void deleteRuleExecutionStats(Long ruleId);
    List<RuleExecutionStats> getAllRuleExecutionStats();
}
package pro.sky.recommendation_service.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.entity.RuleExecutionStats;
import pro.sky.recommendation_service.repository.RuleExecutionStatsRepository;
import pro.sky.recommendation_service.service.RuleExecutionStatsService;

import java.util.List;

@Service
public class RuleExecutionStatsServiceImpl implements RuleExecutionStatsService {
    private final RuleExecutionStatsRepository statsRepository;

    public RuleExecutionStatsServiceImpl(RuleExecutionStatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public void incrementRuleExecutionCount(Long ruleId) {
        RuleExecutionStats stats = statsRepository.findByRuleId(ruleId)
                .orElse(new RuleExecutionStats(ruleId, 0));
        stats.setCount(stats.getCount() + 1);
        statsRepository.save(stats);
    }

    @Override
    public void deleteRuleExecutionStats(Long ruleId) {
        statsRepository.deleteById(ruleId);
    }

    @Override
    public List<RuleExecutionStats> getAllRuleExecutionStats() {
        return statsRepository.findAll();
    }
}

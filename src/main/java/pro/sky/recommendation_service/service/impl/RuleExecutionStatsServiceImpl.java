package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.entity.RuleExecutionStats;
import pro.sky.recommendation_service.repository.RuleExecutionStatsRepository;
import pro.sky.recommendation_service.service.RuleExecutionStatsService;

import java.util.List;

@Service
public class RuleExecutionStatsServiceImpl implements RuleExecutionStatsService {

    private final Logger logger = LoggerFactory.getLogger(RuleExecutionStatsServiceImpl.class);

    private final RuleExecutionStatsRepository statsRepository;

    public RuleExecutionStatsServiceImpl(RuleExecutionStatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public void incrementRuleExecutionCount(Long ruleId) {

        logger.info("Starting incrementing rule execution count for rule: {}", ruleId);

        RuleExecutionStats stats = statsRepository.findByRuleId(ruleId)
                .orElse(new RuleExecutionStats(ruleId, 0));
        stats.setCount(stats.getCount() + 1);
        statsRepository.save(stats);

        logger.info("Successfully added increment of rule execution count for rule: {}", ruleId);
    }

    @Override
    public void deleteRuleExecutionStats(Long ruleId) {

        logger.info("Starting deleting rule execution count for rule: {}", ruleId);
        statsRepository.deleteById(ruleId);

        logger.info("Successfully deleted rule execution count for rule: {}", ruleId);
    }

    @Override
    public List<RuleExecutionStats> getAllRuleExecutionStats() {

        logger.info("Successfully got all rules execution stats");
        return statsRepository.findAll();
    }

}

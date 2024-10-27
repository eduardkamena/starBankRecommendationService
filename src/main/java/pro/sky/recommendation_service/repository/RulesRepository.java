package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.dto.RecommendationRuleDTO;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.repository.mapper.RuleRowMapper;

import java.util.List;
import java.util.UUID;

@Repository
public class RulesRepository {

    private final JdbcTemplate jdbcTemplate;

    public RulesRepository(
            @Qualifier("dynamicsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RecommendationRule> getAllRules(){

        String selectRuleQuery = "SELECT * FROM rules";

        RuleRowMapper rowMapper = new RuleRowMapper(jdbcTemplate);
        jdbcTemplate.query(selectRuleQuery, rowMapper);

        return rowMapper.getRules();

    }

    public void addRule(RecommendationRuleDTO rule){
        String addRule = "INSERT INTO rules (id, query, negate)  VALUES (?, ?, ?)";
        String addArguments = "INSERT INTO arguments (rule_id, index, argument)  VALUES (?, ?, ?)";
        UUID ruleId = UUID.randomUUID();

        jdbcTemplate.update(addRule, ruleId, rule.getQuery().toString(), rule.isNegate());

        List<String> arguments = rule.getArguments();
        int count = 1;
        for (String argument : arguments) {
            jdbcTemplate.update(addArguments, ruleId, count++, argument);
        }

    }

    public void deleteRuleById(UUID ruleId) {
        String deleteSql = "DELETE FROM rules WHERE id = ?";

        jdbcTemplate.update(deleteSql, ruleId);
    }
}

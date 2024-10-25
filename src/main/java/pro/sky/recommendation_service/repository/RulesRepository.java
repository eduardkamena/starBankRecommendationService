package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.entity.Rule;
import pro.sky.recommendation_service.repository.mapper.RuleRowMapper;

import java.util.List;
import java.util.UUID;

@Repository
public class RulesRepository {

    private final JdbcTemplate jdbcTemplate;

    public RulesRepository(
            @Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rule> getAllRules(){
        String sql = "SELECT * FROM rules";

        return jdbcTemplate.query(sql, new RuleRowMapper());
    }

    public void addRule(Rule rule){
        String addSql = "INSERT INTO rules (id, query, arguments, negate)  VALUES (?, ?, ?, ?)";
        UUID ruleId = UUID.randomUUID();

        jdbcTemplate.update(addSql, ruleId, rule.getQuery(), rule.getArguments(), rule.isNegate());

    }

    public void deleteRuleById(UUID ruleId) {
        String deleteSql = "DELETE FROM rules WHERE id = ?";

        jdbcTemplate.update(deleteSql, ruleId);
    }
}

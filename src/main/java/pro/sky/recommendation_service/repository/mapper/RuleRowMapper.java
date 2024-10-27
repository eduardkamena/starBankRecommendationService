package pro.sky.recommendation_service.repository.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.entity.enums.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RuleRowMapper implements RowMapper<RecommendationRule> {
    private final JdbcTemplate jdbcTemplate;

    public RuleRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final Map<UUID, RecommendationRule> ruleMap = new HashMap<>();

    @Override
    public RecommendationRule mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//
//        RecommendationRule rule = new RecommendationRule();
//        UUID ruleId = rs.getObject("id", UUID.class);
//
//        rule.setId(ruleId);
//        rule.setQuery(Queries.valueOf(rs.getString("query")));
////        rule.setArguments((List<String>) rs.getArray("argument").getArray());
//        rule.setNegate(rs.getBoolean("negate"));
//
//        String sql = "SELECT argument FROM arguments WHERE rule_id = ?";
//        List<String> arguments = jdbcTemplate.queryForList(sql, String.class, ruleId);
//        rule.setArguments(arguments);
//
        UUID ruleId = rs.getObject("id", UUID.class);

        RecommendationRule rule = ruleMap.get(ruleId);
        if (rule == null) {
            rule = new RecommendationRule();
            rule.setId(ruleId);
            rule.setQuery(Queries.valueOf(rs.getString("query")));
            rule.setNegate(rs.getBoolean("negate"));
            ruleMap.put(ruleId, rule);
        }

        String argumentQuery = "SELECT argument FROM arguments WHERE rule_id = ?";
        List<String> arguments = jdbcTemplate.queryForList(argumentQuery, String.class, ruleId);
        rule.setArguments(arguments);

        return rule;

    }

    public List<RecommendationRule> getRules() {
        return new ArrayList<>(ruleMap.values());
    }
}
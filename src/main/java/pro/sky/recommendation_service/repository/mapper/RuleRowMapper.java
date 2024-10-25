package pro.sky.recommendation_service.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import pro.sky.recommendation_service.entity.Rule;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RuleRowMapper implements RowMapper<Rule> {
    @Override
    public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rule rule = new Rule();

        rule.setQuery(rs.getString("query"));
        rule.setArguments((String[]) rs.getArray("arguments").getArray());
        rule.setNegate(rs.getBoolean("negate"));

        return rule;
    }
}
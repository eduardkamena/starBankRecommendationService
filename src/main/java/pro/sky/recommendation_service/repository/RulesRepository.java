package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.entity.Rule;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RulesRepository {

    private final JdbcTemplate jdbcTemplate;

    public RulesRepository(
            @Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public class RuleRowMapper implements RowMapper<Rule> {
        @Override
        public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rule rule = new Rule();
            rule.setQuery(rs.getString("query"));
            rule.setArguments(rs.getObject("arguments", String.class));
            rule.setNegate(rs.getBoolean("negate"));

            return rule;
        }
    }

    public List<Rule> getAllRules(){
        DataSource dataSource = jdbcTemplate.getDataSource();

        RuleRowMapper ruleRowMapper = new RuleRowMapper();

        List<Rule> rules = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM \"RulesData\"")) {
            while (resultSet.next()) {
                Rule rule = ruleRowMapper.mapRow(resultSet, rules.size());
                rules.add(rule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rules;
    }
}

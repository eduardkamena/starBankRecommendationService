package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.entity.Rule;
import pro.sky.recommendation_service.repository.mapper.RuleRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RulesRepository {

    private final JdbcTemplate jdbcTemplate;

    RuleRowMapper ruleRowMapper;

    public RulesRepository(
            @Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rule> getAllRules(){
        List<Rule> rules = new ArrayList<>();
        DataSource dataSource = jdbcTemplate.getDataSource();

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

    public void deleteRuleById(String id) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        String deleteSql = "DELETE FROM \"RulesData\" WHERE \"RuleId\" = ?";

        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(deleteSql);
            statement.setString(1, id);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

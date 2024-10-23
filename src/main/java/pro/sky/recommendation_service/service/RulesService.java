package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.entity.Rule;

import java.sql.SQLException;
import java.util.List;

public interface RulesService {

    List<Rule> getAllRules() throws SQLException;
    Rule createRule(Rule rule);
    void deleteRule(String id);

}

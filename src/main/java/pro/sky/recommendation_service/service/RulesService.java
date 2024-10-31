package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RulesDTO;

import java.util.List;
import java.util.Optional;

public interface RulesService {

    RulesDTO createRule(RulesDTO rulesDTO);

    void deleteRule(String query);

    Optional<List<RulesDTO>> readAllRules();

}

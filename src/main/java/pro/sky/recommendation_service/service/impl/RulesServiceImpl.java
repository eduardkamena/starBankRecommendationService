package pro.sky.recommendation_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.enums.RulesArgumentsENUM;
import pro.sky.recommendation_service.enums.RulesQueryENUM;
import pro.sky.recommendation_service.repository.RulesRepository;
import pro.sky.recommendation_service.service.RulesService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RulesServiceImpl implements RulesService {

    private final RulesDTO rules;
    private final RulesRepository rulesRepository;

    @Autowired
    public RulesServiceImpl(RulesDTO rules, RulesRepository rulesRepository) {
        this.rules = rules;
        this.rulesRepository = rulesRepository;
    }

    @Override
    public RulesDTO createRule(RulesDTO rulesDTO) {
        checkArguments(rulesDTO);
        return rulesRepository.createRule(rulesDTO);
    }

    @Override
    public void deleteRule(String query) {
        checkQuery(query);
        rulesRepository.deleteRule(query);
    }

    @Override
    public Optional<List<RulesDTO>> readAllRules() {
        return rulesRepository.readAllRules();
    }

    public boolean checkQuery(String query) {
        if (Arrays.stream(RulesQueryENUM.values()).noneMatch(e -> e.name().equals(query))) {
            throw new IllegalArgumentException("Query should contains in RulesQueryENUM");
        } else return true;
    }

    public boolean checkArguments(RulesDTO rulesDTO) {

        if (rulesDTO.getQuery() == null
                || rulesDTO.getQuery().isEmpty()
                || Arrays.stream(RulesQueryENUM.values()).noneMatch(e -> e.name().equals(rulesDTO.getQuery().toUpperCase()))) {
            throw new IllegalArgumentException("Query cannot be null or empty and should contains in RulesQueryENUM");
        }

        String[] arguments = rulesDTO.getArguments();
        if (arguments == null
                || arguments.length == 0) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        String argumentsStr = Arrays.toString(arguments).toUpperCase();

        if (Arrays.stream(RulesArgumentsENUM.values()).anyMatch(e -> argumentsStr.contains(e.name()))
                || argumentsStr.contains(">")
                || argumentsStr.contains("<")
                || argumentsStr.contains(">=")
                || argumentsStr.contains("<=")
                || argumentsStr.contains("=")
        ) {
            return true;
        } else {
            throw new IllegalArgumentException("Rules must have at least one argument or contains in RulesArgumentsENUM");
        }
    }

}

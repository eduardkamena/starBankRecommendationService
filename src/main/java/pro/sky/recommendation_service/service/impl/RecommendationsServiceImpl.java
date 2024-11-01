package pro.sky.recommendation_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.entity.Rules;
import pro.sky.recommendation_service.enums.RulesArgumentsENUM;
import pro.sky.recommendation_service.enums.RulesQueryENUM;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.service.RecommendationsService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {

    private final RecommendationsRepository recommendationsRepository;

    @Autowired
    public RecommendationsServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Recommendations createRecommendation(RecommendationsDTO recommendationsDTO) {
        Recommendations recommendation = new Recommendations();
        recommendation.setProduct_name(recommendationsDTO.getProduct_name());
        recommendation.setProduct_id(recommendationsDTO.getProduct_id());
        recommendation.setProduct_text(recommendationsDTO.getProduct_text());

        List<Rules> rules = recommendationsDTO.getRule().stream()
                .map(ruleDTO -> {

                    checkQuery(ruleDTO.getQuery().toUpperCase());
                    checkArguments(ruleDTO.getArguments());

                    Rules rule = new Rules();
                    rule.setQuery(ruleDTO.getQuery().toUpperCase());
                    rule.setArguments(ruleDTO.getArguments()
                            .stream()
                            .map(String::toUpperCase)
                            .collect(Collectors.toList()));
                    rule.setNegate(ruleDTO.isNegate());
                    rule.setRecommendations(recommendation);
                    return rule;
                })
                .collect(Collectors.toList());

        recommendation.setRule(rules);

        return recommendationsRepository.save(recommendation);
    }

    @Override
    public Optional<RecommendationsDTO> readRule(UUID id) {
        return recommendationsRepository.findById(id)
                .map(recommendation -> {
                    RecommendationsDTO dto = new RecommendationsDTO();
                    dto.setId(recommendation.getId());
                    dto.setProduct_name(recommendation.getProduct_name());
                    dto.setProduct_id(recommendation.getProduct_id());
                    dto.setProduct_text(recommendation.getProduct_text());

                    List<RulesDTO> rulesDTO = recommendation.getRule().stream()
                            .map(rule -> {
                                RulesDTO ruleDTO = new RulesDTO();
                                ruleDTO.setId(rule.getId());
                                ruleDTO.setQuery(rule.getQuery());
                                ruleDTO.setArguments(rule.getArguments());
                                ruleDTO.setNegate(rule.isNegate());
                                return ruleDTO;
                            })
                            .collect(Collectors.toList());

                    dto.setRule(rulesDTO);
                    return dto;
                });
    }

    @Override
    public void deleteRule(UUID id) {
        recommendationsRepository.deleteById(id);
    }

    public void checkQuery(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }

        if (Arrays.stream(RulesQueryENUM.values()).noneMatch(e -> e.name().equals(query.toUpperCase()))) {
            throw new IllegalArgumentException("Query should be one of the values in RulesQueryENUM");
        }
    }

    public boolean checkArguments(List<String> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        List<String> argumentsStr = arguments.stream()
                .map(String::toUpperCase)
                .toList();

        String[] VALID_OPERATORS = {">", "<", ">=", "<=", "="};

        boolean containsValidArgument = Arrays.stream(RulesArgumentsENUM.values())
                .anyMatch(e -> argumentsStr.contains(e.name()))
                || Arrays.stream(VALID_OPERATORS)
                .anyMatch(argumentsStr::contains);

        if (!containsValidArgument) {
            throw new IllegalArgumentException("Rules must have at least one argument or contains in RulesArgumentsENUM");
        }

        return true;
    }

}

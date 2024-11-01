package pro.sky.recommendation_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.entity.Rules;
import pro.sky.recommendation_service.enums.RulesQueryENUM;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.service.RecommendationsService;

import java.util.Arrays;
import java.util.List;
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
                    Rules rule = new Rules();
                    rule.setQuery(ruleDTO.getQuery().toUpperCase());
                    rule.setArguments(String.join(",", ruleDTO.getArguments()).toUpperCase());
                    rule.setNegate(ruleDTO.isNegate());
                    rule.setRecommendations(recommendation);
                    return rule;
                })
                .collect(Collectors.toList());

        recommendation.setRule(rules);

        return recommendationsRepository.save(recommendation);
    }

    @Override
    public Recommendations readRule(UUID id) {
        return recommendationsRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteRule(UUID id) {
        recommendationsRepository.deleteById(id);
    }

    public boolean checkQuery(String query) {
        if (Arrays.stream(RulesQueryENUM.values()).noneMatch(e -> e.name().equals(query))) {
            throw new IllegalArgumentException("Query should contains in RulesQueryENUM");
        } else return true;
    }

//    public boolean checkArguments(Rules rules) {
//
//        if (rules.getQuery() == null
//                || rules.getQuery().isEmpty()
//                || Arrays.stream(RulesQueryENUM.values()).noneMatch(e -> e.name().equals(rules.getQuery().toUpperCase()))) {
//            throw new IllegalArgumentException("Query cannot be null or empty and should contain in RulesQueryENUM");
//        }
//
//        String[] arguments = rules.getArguments();
//        if (arguments == null
//                || arguments.length == 0) {
//            throw new IllegalArgumentException("Arguments cannot be null or empty");
//        }
//
//        String argumentsStr = Arrays.toString(arguments).toUpperCase();
//
//        if (Arrays.stream(RulesArgumentsENUM.values()).anyMatch(e -> argumentsStr.contains(e.name()))
//                || argumentsStr.contains(">")
//                || argumentsStr.contains("<")
//                || argumentsStr.contains(">=")
//                || argumentsStr.contains("<=")
//                || argumentsStr.contains("=")
//        ) {
//            return true;
//        } else {
//            throw new IllegalArgumentException("Rules must have at least one argument or contains in RulesArgumentsENUM");
//        }
//    }

}

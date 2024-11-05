package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.entity.Rules;
import pro.sky.recommendation_service.enums.RulesArgumentsENUM;
import pro.sky.recommendation_service.enums.RulesQueryENUM;
import pro.sky.recommendation_service.exception.DoesNotEnumException;
import pro.sky.recommendation_service.exception.NullOrEmptyException;
import pro.sky.recommendation_service.exception.RuleNotFoundException;
import pro.sky.recommendation_service.repository.DynamicJPARecommendationsRepository;
import pro.sky.recommendation_service.service.DynamicRulesRecommendationsService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamicRulesRecommendationsServiceImpl implements DynamicRulesRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(DynamicRulesRecommendationsServiceImpl.class);

    private final DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository;

    @Autowired
    public DynamicRulesRecommendationsServiceImpl(DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository) {
        this.dynamicJPARecommendationsRepository = dynamicJPARecommendationsRepository;
    }

    @Override
    public Recommendations createDynamicRuleRecommendation(RecommendationsDTO recommendationsDTO) {

        logger.info("Starting adding recommendation in database for recommendations: {}", recommendationsDTO);
        Recommendations recommendation = new Recommendations();
        recommendation.setProduct_name(recommendationsDTO.getProduct_name());
        recommendation.setProduct_id(recommendationsDTO.getProduct_id());
        recommendation.setProduct_text(recommendationsDTO.getProduct_text());
        logger.info("Successfully added recommendation in database for recommendation: {}", recommendation);

        logger.info("Starting adding rules in database for recommendations: {}", recommendationsDTO);
        List<Rules> rules = recommendationsDTO.getRule().stream()
                .map(ruleDTO -> {

                    // Проверка на принадлежность к ENUM
                    logger.info("Start checking query and arguments for adding rule: {}", ruleDTO);
                    checkQuery(ruleDTO.getQuery().toUpperCase());
                    checkArguments(ruleDTO.getArguments());
                    logger.info("End checking query and arguments for adding rule: {}", ruleDTO);

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

        logger.info("Successfully added rules in database for recommendation");
        return dynamicJPARecommendationsRepository.save(recommendation);
    }

    @Override
    public Optional<RecommendationsDTO> getDynamicRuleRecommendation(UUID recommendation_id) throws RuleNotFoundException {

        logger.info("Starting checking dynamic rule in database for id: {}", recommendation_id);
        if (dynamicJPARecommendationsRepository.existsById(recommendation_id)) {

            logger.info("Starting getting recommendation from database for recommendation: {}", recommendation_id);
            return dynamicJPARecommendationsRepository.findById(recommendation_id)
                    .map(recommendation -> {
                        RecommendationsDTO dto = new RecommendationsDTO();
                        dto.setId(recommendation.getId());
                        dto.setProduct_name(recommendation.getProduct_name());
                        dto.setProduct_id(recommendation.getProduct_id());
                        dto.setProduct_text(recommendation.getProduct_text());

                        List<RulesDTO> rulesDTO = recommendation.getRule()
                                .stream()
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
                        logger.info("Successfully got recommendation from database for recommendation");
                        return dto;
                    });
        }
        logger.error("Error checking dynamic rule in database for id: {}", recommendation_id);
        throw new RuleNotFoundException("Dynamic rule not found in database");
    }

    @Override
    public List<RecommendationsDTO> getAllDynamicRulesRecommendations() {

        logger.info("Starting getting all recommendations from database");
        return dynamicJPARecommendationsRepository.findAll()
                .stream()
                .map(recommendation -> {
                    RecommendationsDTO dto = new RecommendationsDTO();
                    dto.setId(recommendation.getId());
                    dto.setProduct_name(recommendation.getProduct_name());
                    dto.setProduct_id(recommendation.getProduct_id());
                    dto.setProduct_text(recommendation.getProduct_text());

                    List<RulesDTO> rulesDTO = recommendation.getRule()
                            .stream()
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
                    logger.info("Successfully got all recommendations from database");
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDynamicRuleRecommendation(UUID recommendation_id) throws RuleNotFoundException {

        logger.info("Starting checking dynamic rule in database for id: {}", recommendation_id);
        if (dynamicJPARecommendationsRepository.existsById(recommendation_id)) {

            dynamicJPARecommendationsRepository.deleteById(recommendation_id);
            logger.info("Rule with id: {} was successfully deleted from database", recommendation_id);
        } else {
            logger.error("Error checking dynamic rule in database for id: {}", recommendation_id);
            throw new RuleNotFoundException("Dynamic rule not found in database");
        }
    }

    public void checkQuery(String query) throws DoesNotEnumException, NullOrEmptyException {
        if (query == null || query.isEmpty()) {
            throw new NullOrEmptyException("Query cannot be null or empty");
        }

        if (Arrays.stream(RulesQueryENUM.values()).noneMatch(e -> e.name().equals(query.toUpperCase()))) {
            throw new DoesNotEnumException("Query should be one of the values in RulesQueryENUM");
        }
    }

    public boolean checkArguments(List<String> arguments) throws DoesNotEnumException, NullOrEmptyException {
        if (arguments == null || arguments.isEmpty()) {
            throw new NullOrEmptyException("Arguments cannot be null or empty");
        }

        List<String> argumentsStr = arguments
                .stream()
                .map(String::toUpperCase)
                .toList();

        String[] VALID_OPERATORS = {">", "<", ">=", "<=", "="};

        boolean containsValidArgument = Arrays.stream(RulesArgumentsENUM.values())
                .anyMatch(e -> argumentsStr.contains(e.name()))
                || Arrays.stream(VALID_OPERATORS)
                .anyMatch(argumentsStr::contains);

        if (!containsValidArgument) {
            throw new DoesNotEnumException("Rules must have at least one argument or contains in RulesArgumentsENUM");
        }

        return true;
    }

}

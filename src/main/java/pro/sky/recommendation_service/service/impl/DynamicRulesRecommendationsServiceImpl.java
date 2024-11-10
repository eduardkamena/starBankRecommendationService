package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.entity.Rules;
import pro.sky.recommendation_service.enums.rulesArgumentsENUM.ComparisonOperators;
import pro.sky.recommendation_service.enums.rulesArgumentsENUM.SumCompare;
import pro.sky.recommendation_service.enums.rulesArgumentsENUM.TransactionProductTypes;
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

    /**
     * Создание динамической рекомендации продукта в БД.
     * <p>
     *      Метод позволяет создать запись динамического правила для продукта рекомендации
     *      в БД. Получаемый объект DTO преобразуется к объекту сущности {@link Recommendations}
     *      и сохраняется в БД
     * </p>
     * @param recommendationsDTO ({@link RecommendationsDTO})
     */
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
                    checkArguments(ruleDTO.getArguments());
                    logger.info("End checking query and arguments for adding rule: {}", ruleDTO);

                    Rules rule = new Rules();
                    rule.setQuery(ruleDTO.getQuery());
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

    /**
     * Получение динамической рекомендации по ID.
     * <p>
     *      Метод позволяет получить запись динамического правила из БД по ID,
     *      с последующим преобразованием к объекту DTO
     * </p>
     * @param recommendation_id ID динамической рекомендации ({@link Recommendations})
     * @return объект DTO ({@link RecommendationsDTO})
     * @throws RuleNotFoundException если рекомендация не найдена в БД
     */
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

    /**
     * Получение всех динамических рекомендаций из БД.
     * <p>
     *     Метод позволяет получить запись всех динамических правил из БД,
     *     с последующим преобразованием к списку объектов DTO
     * </p>
     * @return список всех рекомендаций приведенных к виду ({@link RecommendationsDTO})
     */
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

    /**
     * Удаление динамической рекомендации из БД.
     *
     * @param recommendation_id ID динамической рекомендации ({@link Recommendations})
     * @throws RuleNotFoundException если рекомендация не найдена в БД
     */
    @Override
    public void deleteDynamicRuleRecommendation(UUID recommendation_id) throws RuleNotFoundException {

        logger.info("Starting checking dynamic rule for deleting in database for id: {}", recommendation_id);
        if (dynamicJPARecommendationsRepository.existsById(recommendation_id)) {

            dynamicJPARecommendationsRepository.deleteById(recommendation_id);
            logger.info("Rule with id: {} was successfully deleted from database", recommendation_id);
        } else {
            logger.error("Error checking dynamic rule in database for deleting for id: {}", recommendation_id);
            throw new RuleNotFoundException("Dynamic rule not found in database");
        }
    }

    /**
     * Проверка соответствия передаваемых аргументов.
     * <p>
     *     Метод выполняющий проверку передаваемых аргументов правил динамической
     *     рекомендации на соответствие с заданными.
     *     <br>Метод выполняет последовательные проверки всего списка
     *     используя {@link DynamicRulesRecommendationsServiceImpl#isValidEnumArguments}
     *
     * @param arguments список аргументов динамического правила рекомендации {@link Rules}
     * @throws IllegalArgumentException если аргумент правила не соответствует заданным
     */

    private void checkArguments(List<String> arguments) throws IllegalArgumentException {
        for (String argument : arguments) {
            if (!isValidEnumArguments(argument)) {
                logger.error("Error argument must be valid ENUMs value");
                throw new IllegalArgumentException("Argument \"" + argument + "\" should be one of the class's values in rulesArgumentsENUM");
            }
        }
    }

    /**
     * Метод последовательной проверки аргумента правил.
     * <p>
     *     Метод использует последовательное сравнение на принадлежность
     *     заданным значениям используя следующие методы:
     *     <ul>
     *         <li>{@link DynamicRulesRecommendationsServiceImpl#isTransactionProductTypeENUM}</li>
     *         <li>{@link DynamicRulesRecommendationsServiceImpl#isComparisonOperatorsENUM}</li>
     *         <li>{@link DynamicRulesRecommendationsServiceImpl#isSumCompareENUM}</li>
     *     </ul>
     * @param arguments аргумент из списка правил динамической рекомендации {@link Rules}
     * @return true или false если найдено или отсутствует совпадение
     */
    private boolean isValidEnumArguments(String arguments) {
        return isTransactionProductTypeENUM(arguments)
                || isComparisonOperatorsENUM(arguments)
                || isSumCompareENUM(arguments);
    }

    /**
     * Метод проверки аргумента правил на соответствие Product Type
     * <p>
     *     Возможные варианты значений можно увидеть:{@link TransactionProductTypes}
     * @param arguments аргумент из списка правил динамической рекомендации {@link Rules}
     * @return true или false если найдено или отсутствует совпадение
     */
    private boolean isTransactionProductTypeENUM(String arguments) throws IllegalArgumentException {
        try {
            TransactionProductTypes.valueOf(arguments.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Метод проверки аргумента правил на соответствие Comparison Operator
     * <p>
     *     Возможные варианты значений можно увидеть:{@link ComparisonOperators}
     * @param arguments аргумент из списка правил динамической рекомендации {@link Rules}
     * @return true или false если найдено или отсутствует совпадение
     */
    private boolean isComparisonOperatorsENUM(String arguments) {
        return Arrays.stream(ComparisonOperators.values())
                .map(ComparisonOperators::getOperatorVal)
                .anyMatch(operator -> operator.equals(arguments));
    }

    /**
     * Метод проверки аргумента правил на соответствие Sum Compare
     * <p>
     *     Возможные варианты значений можно увидеть:{@link SumCompare}
     * @param arguments аргумент из списка правил динамической рекомендации {@link Rules}
     * @return true или false если найдено или отсутствует совпадение
     */
    private boolean isSumCompareENUM(String arguments) {
        return Arrays.stream(SumCompare.values())
                .map(sum -> String.valueOf(sum.getSumVal()))
                .anyMatch(sumVal -> sumVal.equals(arguments));
    }

}

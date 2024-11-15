package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.configuration.DataSourceConfig;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.entity.Rules;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.DynamicJDBCRecommendationsRepository;
import pro.sky.recommendation_service.repository.DynamicJPARecommendationsRepository;
import pro.sky.recommendation_service.repository.ProductRecommendationsRepository;
import pro.sky.recommendation_service.repository.RulesRecommendationsRepository;
import pro.sky.recommendation_service.service.StatsService;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <h3>Класс, реализующий работу по выдаче пользователю
 * динамических рекомендаций пользователю.
 * <p>
 * Класс по выдаче рекомендаций пользователю позволяет использовать
 * следующий функционал:
 * <ln>
 * <li>Поиск клиента по БД
 * см.{@link UserDynamicRecommendationsServiceImpl#checkIsUserExists checkIsUserExists}</li>
 * <li>Получение всех доступных рекомендаций у пользователя
 * см.{@link UserDynamicRecommendationsServiceImpl#getAllDynamicRecommendations getAllDynamicRecommendations}</li>
 * <li>Получение в Telegram всех доступных рекомендаций у пользователя
 * см.{@link UserDynamicRecommendationsServiceImpl#getAllDynamicRulesRecommendationsForTelegramBot getAllDynamicRulesRecommendationsForTelegramBot}</li>
 * <li>Поиск возможных рекомендаций для предложения клиенту
 * см.{@link UserDynamicRecommendationsServiceImpl#checkUserDynamicRecommendations checkUserDynamicRecommendations}</li>
 * </ln>
 */
@Service
public class UserDynamicRecommendationsServiceImpl implements UserDynamicRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(UserDynamicRecommendationsServiceImpl.class);

    private final DynamicJDBCRecommendationsRepository dynamicJDBCRecommendationsRepository;
    private final DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository;
    private final RulesRecommendationsRepository rulesRecommendationsRepository;
    private final ProductRecommendationsRepository productRecommendationsRepository;
    private final StatsService statsService;

    public UserDynamicRecommendationsServiceImpl(DynamicJDBCRecommendationsRepository dynamicJDBCRecommendationsRepository,
                                                 DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository,
                                                 RulesRecommendationsRepository rulesRecommendationsRepository,
                                                 ProductRecommendationsRepository productRecommendationsRepository,
                                                 StatsService statsService) {
        this.dynamicJDBCRecommendationsRepository = dynamicJDBCRecommendationsRepository;
        this.dynamicJPARecommendationsRepository = dynamicJPARecommendationsRepository;
        this.rulesRecommendationsRepository = rulesRecommendationsRepository;
        this.productRecommendationsRepository = productRecommendationsRepository;
        this.statsService = statsService;
    }

    /**
     * Метод позволяет получить список доступных рекомендаций
     * для пользователя, после прохождения проверки на его наличие в БД.
     *
     * @param userId идентификатор пользователя в БД
     *               ({@link pro.sky.recommendation_service.configuration.DataSourceConfig#recommendationsJdbcTemplate recommendationsJdbcTemplate})
     * @return пользователь со списком рекомендаций
     * ({@link UserRecommendationsDTO})
     * @throws UserNotFoundException если пользователь не найден в БД
     */
    @Override
    @Cacheable(cacheNames = "dynamicRecommendations", key = "#root.methodName + #userId.toString()")
    public UserRecommendationsDTO getAllDynamicRecommendations(UUID userId) throws UserNotFoundException {

        logger.info("Starting executing all dynamic recommendations for userId: {}", userId);

        checkIsUserExists(userId);

        List<ProductRecommendationsDTO> recommendations = checkUserDynamicRecommendations(userId);

        logger.info("Transferring all found recommendations from List<> to UserRecommendationsDTO for userId: {}", userId);
        return new UserRecommendationsDTO(userId, recommendations);

    }

    /**
     * Метод позволяет получить список доступных рекомендаций
     * для пользователя, после прохождения проверки на его наличие в БД,
     * с последующим преобразованием к строке, подготовленной для передачи
     * сообщения в Telegram.
     *
     * @param userId идентификатор пользователя в БД
     *               ({@link DataSourceConfig#recommendationsJdbcTemplate recommendationsJdbcTemplate})
     * @return строка, содержащая все доступные пользователю рекомендации
     */
    @Override
    @Cacheable(cacheNames = "telegramBot", key = "#root.methodName + #userId.toString()")
    public String getAllDynamicRulesRecommendationsForTelegramBot(UUID userId) {

        logger.info("Starting executing all dynamic recommendations fot TelegramBot for userId: {}", userId);

        List<ProductRecommendationsDTO> recommendations = checkUserDynamicRecommendations(userId);

        StringBuilder sb = new StringBuilder();

        for (ProductRecommendationsDTO recommendation : recommendations) {
            sb.append("*Название продукта:* ").append(recommendation.getProductName()).append("\n\n");
            sb.append("*Описание продукта:* \n").append(recommendation.getProductText()).append("\n\n\n");
        }
        logger.info("Forwarding all dynamic recommendations in String for TelegramBot for userId: {}", userId);
        return sb.toString();
    }

    /**
     * Метод выполняет проверку нахождения пользователя в БД
     *
     * @param userId идентификатор пользователя в БД
     * @throws UserNotFoundException если пользователь с данным ID не найден в БД
     * @throws NullPointerException  если не был передан ID
     */
    private void checkIsUserExists(UUID userId) throws UserNotFoundException, NullPointerException {

        logger.info("Starting checking user in database for userId: {}", userId);
        boolean flag = dynamicJDBCRecommendationsRepository.isUserExists(userId);

        if (userId == null) {
            logger.error("Error (NullPointer) checking user in database for userId");
            throw new NullPointerException("User is null");

        } else if (!flag) {
            logger.error("Error (UserNotFound) checking user in database for userId: {}", userId);
            throw new UserNotFoundException("User not found in database");
        }

        logger.info("User with id {} successfully exists", userId);
    }

    /**
     * Метод выполняет проверку всех возможных рекомендаций
     * для предложения пользователю.
     * <p>
     * Структура работы:
     * <ol>
     *     <li>Получение всех рекомендаций из БД</li>
     *     <li>Проверка выполнения правил каждой из рекомендаций для пользователя</li>
     *     <li>Увеличение счетчика сработки рекомендации, если она доступна пользователю</li>
     *     <li>Добавление и возврат списка всех доступных рекомендаций</li>
     * </ol>
     *
     * @param userId идентификатор пользователя в БД
     * @return список (List) всех доступных рекомендаций для пользователя
     */

    private List<ProductRecommendationsDTO> checkUserDynamicRecommendations(UUID userId) {

        List<ProductRecommendationsDTO> recommendations = new ArrayList<>();
        List<UUID> foundAllRecommendationsInDB = dynamicJPARecommendationsRepository.findAllRecommendationsIDs();
        logger.info("List's size of checking dynamic recommendations: {}", foundAllRecommendationsInDB.size());

        for (UUID recommendationId : foundAllRecommendationsInDB) {

            logger.info("Start checking rules of dynamic recommendation: {}", recommendationId);

            boolean allCasesMatched = true; // Флаг для отслеживания совпадений всех case
            List<Rules> checkingRecommendationsRules = rulesRecommendationsRepository
                    .findByRecommendationsId(recommendationId);

            logger.info("Number of rules - {} - for checking of dynamic recommendation: {}", checkingRecommendationsRules.size(), recommendationId);

            for (Rules rule : checkingRecommendationsRules) {
                boolean result = switch (rule.getQuery()) {
                    case USER_OF -> dynamicJDBCRecommendationsRepository
                            .isUserOf(userId, rule.getArguments());
                    case ACTIVE_USER_OF -> dynamicJDBCRecommendationsRepository
                            .isActiveUserOf(userId, rule.getArguments());
                    case TRANSACTION_SUM_COMPARE -> dynamicJDBCRecommendationsRepository
                            .isTransactionSumCompare(userId, rule.getArguments());
                    case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> dynamicJDBCRecommendationsRepository
                            .isTransactionSumCompareDepositWithdraw(userId, rule.getArguments());
                };

                if (result != rule.isNegate()) {
                    allCasesMatched = false;
                    logger.warn("Rule {} did not match for recommendationId - {}", rule.getQuery(), recommendationId);
                    break;
                }
            }

            if (allCasesMatched) {

                statsService.incrementStatsCount(recommendationId);

                logger.info("Adding result of getting recommendation {} to List<> for userId: {}", recommendationId, userId);
                List<ProductRecommendationsDTO> matchedRecommendations = productRecommendationsRepository.findById(recommendationId)
                        .stream()
                        .map(recommendation -> {
                            ProductRecommendationsDTO dto = new ProductRecommendationsDTO();
                            dto.setProductName(recommendation.getProductName());
                            dto.setProductId(recommendation.getProductId());
                            dto.setProductText(recommendation.getProductText());
                            return dto;
                        }).toList();

                recommendations.addAll(matchedRecommendations);
                logger.info("Recommendations successfully added for user: {}", userId);
            }
        }
        return recommendations;
    }

}

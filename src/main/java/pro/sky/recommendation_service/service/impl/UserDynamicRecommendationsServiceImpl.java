package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
 *     Класс по выдаче рекомендаций пользователю позволяет использовать
 *     следующий функционал:
 *     <ln>
 *         <li>Поиск клиента по БД
 *              см.{@link UserDynamicRecommendationsServiceImpl#checkIsUserExists checkIsUserExists}</li>
 *         <li>Получение всех доступных рекомендаций у пользователя
 *              см.{@link UserDynamicRecommendationsServiceImpl#getAllDynamicRecommendations getAllDynamicRecommendations}</li>
 *         <li>Получение в Telegram всех доступных рекомендаций у пользователя
 *              см.{@link UserDynamicRecommendationsServiceImpl#getAllDynamicRulesRecommendationsForTelegramBot getAllDynamicRulesRecommendationsForTelegramBot}</li>
 *         <li>Поиск возможных рекомендаций для предложения клиенту
 *              см.{@link UserDynamicRecommendationsServiceImpl#checkUserDynamicRecommendations checkUserDynamicRecommendations}</li>
 *     </ln>
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
     * @param user_id идентификатор пользователя в БД
     *      ({@link pro.sky.recommendation_service.configuration.DataSourceConfig#recommendationsJdbcTemplate recommendationsJdbcTemplate})
     * @return пользователь со списком рекомендаций
     *      ({@link UserRecommendationsDTO})
     * @throws UserNotFoundException если пользователь не найден в БД
     */
    @Override
    public UserRecommendationsDTO getAllDynamicRecommendations(UUID user_id) throws UserNotFoundException {

        logger.info("Starting executing all dynamic recommendations for user_id: {}", user_id);

        checkIsUserExists(user_id);

        List<ProductRecommendationsDTO> recommendations = checkUserDynamicRecommendations(user_id);

        logger.info("Transferring all found recommendations from List<> to UserRecommendationsDTO for user_id: {}", user_id);
        return new UserRecommendationsDTO(user_id, recommendations);

    }

    /**
     * Метод позволяет получить список доступных рекомендаций
     * для пользователя, после прохождения проверки на его наличие в БД,
     * с последующим преобразованием к строке, подготовленной для передачи
     * сообщения в Telegram.
     *
     * @param user_id идентификатор пользователя в БД
     *      ({@link pro.sky.recommendation_service.configuration.DataSourceConfig#recommendationsJdbcTemplate recommendationsJdbcTemplate})
     * @return строка, содержащая все доступные пользователю рекомендации
     */
    @Override
    public String getAllDynamicRulesRecommendationsForTelegramBot(UUID user_id) {

        logger.info("Starting executing all dynamic recommendations fot TelegramBot for user_id: {}", user_id);

        List<ProductRecommendationsDTO> recommendations = checkUserDynamicRecommendations(user_id);

        StringBuilder sb = new StringBuilder();

        for (ProductRecommendationsDTO recommendation : recommendations) {
            sb.append("*Название продукта:* ").append(recommendation.getProduct_name()).append("\n\n");
            sb.append("*Описание продукта:* \n").append(recommendation.getProduct_text()).append("\n\n\n");
        }
        logger.info("Forwarding all dynamic recommendations in String for TelegramBot for user_id: {}", user_id);
        return sb.toString();
    }

    /**
     * Метод выполняет проверку нахождения пользователя в БД
     *
     * @param user_id идентификатор пользователя в БД
     * @throws UserNotFoundException если пользователь с данным ID не найден в БД
     * @throws NullPointerException если не был передан ID
     */
    private void checkIsUserExists(UUID user_id) throws UserNotFoundException, NullPointerException {

        logger.info("Starting checking user in database for user_id: {}", user_id);
        boolean flag = dynamicJDBCRecommendationsRepository.isUserExists(user_id);

        if (user_id == null) {
            logger.error("Error (NullPointer) checking user in database for user_id");
            throw new NullPointerException("User is null");

        } else if (!flag) {
            logger.error("Error (UserNotFound) checking user in database for user_id: {}", user_id);
            throw new UserNotFoundException("User not found in database");
        }

        logger.info("User with id {} successfully exists", user_id);
    }

    /**
     * Метод выполняет проверку всех возможных рекомендаций
     * для предложения пользователю.
     * <p>
     *     Структура работы:
     * <ol>
     *     <li>Получение всех рекомендаций из БД</li>
     *     <li>Проверка выполнения правил каждой из рекомендаций для пользователя</li>
     *     <li>Увеличение счетчика сработки рекомендации, если она доступна пользователю</li>
     *     <li>Добавление и возврат списка всех доступных рекомендаций</li>
     * </ol>
     *
     * @param user_id идентификатор пользователя в БД
     * @return список (List) всех доступных рекомендаций для пользователя
     */
    private List<ProductRecommendationsDTO> checkUserDynamicRecommendations(UUID user_id) {

        List<ProductRecommendationsDTO> recommendations = new ArrayList<>();
        List<UUID> foundAllRecommendationsInDB = dynamicJPARecommendationsRepository.findAllRecommendationsIDs();
        logger.info("List's size of checking dynamic recommendations: {}", foundAllRecommendationsInDB.size());

        for (UUID recommendation_id : foundAllRecommendationsInDB) {

            logger.info("Start checking rules of dynamic recommendation: {}", recommendation_id);

            boolean allCasesMatched = true; // Флаг для отслеживания совпадений всех case
            List<Rules> checkingRecommendationsRules = rulesRecommendationsRepository
                    .findByRecommendationsId(recommendation_id);

            logger.info("Number of rules - {} - for checking of dynamic recommendation: {}", checkingRecommendationsRules.size(), recommendation_id);

            for (Rules rule : checkingRecommendationsRules) {
                boolean result = switch (rule.getQuery()) {
                    case USER_OF -> dynamicJDBCRecommendationsRepository
                            .isUserOf(user_id, rule.getArguments());
                    case ACTIVE_USER_OF -> dynamicJDBCRecommendationsRepository
                            .isActiveUserOf(user_id, rule.getArguments());
                    case TRANSACTION_SUM_COMPARE -> dynamicJDBCRecommendationsRepository
                            .isTransactionSumCompare(user_id, rule.getArguments());
                    case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> dynamicJDBCRecommendationsRepository
                            .isTransactionSumCompareDepositWithdraw(user_id, rule.getArguments());
                };

                if (result != rule.isNegate()) {
                    allCasesMatched = false;
                    logger.warn("Rule {} did not match for recommendation id - {}", rule.getQuery(), recommendation_id);
                    break;
                }
            }

            if (allCasesMatched) {

                statsService.incrementStatsCount(recommendation_id);

                logger.info("Adding result of getting recommendation {} to List<> for user_id: {}", recommendation_id, user_id);
                List<ProductRecommendationsDTO> matchedRecommendations = productRecommendationsRepository.findById(recommendation_id)
                        .stream()
                        .map(recommendation -> {
                            ProductRecommendationsDTO dto = new ProductRecommendationsDTO();
                            dto.setProduct_name(recommendation.getProduct_name());
                            dto.setProduct_id(recommendation.getProduct_id());
                            dto.setProduct_text(recommendation.getProduct_text());
                            return dto;
                        }).toList();

                recommendations.addAll(matchedRecommendations);
                logger.info("Recommendations successfully added for user: {}", user_id);
            }
        }
        return recommendations;
    }

}

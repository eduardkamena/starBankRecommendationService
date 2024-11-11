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
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserDynamicRecommendationsServiceImpl implements UserDynamicRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(UserDynamicRecommendationsServiceImpl.class);

    private final DynamicJDBCRecommendationsRepository dynamicJDBCRecommendationsRepository;
    private final DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository;
    private final RulesRecommendationsRepository rulesRecommendationsRepository;
    private final ProductRecommendationsRepository productRecommendationsRepository;

    public UserDynamicRecommendationsServiceImpl(DynamicJDBCRecommendationsRepository dynamicJDBCRecommendationsRepository,
                                                 DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository,
                                                 RulesRecommendationsRepository rulesRecommendationsRepository,
                                                 ProductRecommendationsRepository productRecommendationsRepository) {
        this.dynamicJDBCRecommendationsRepository = dynamicJDBCRecommendationsRepository;
        this.dynamicJPARecommendationsRepository = dynamicJPARecommendationsRepository;
        this.rulesRecommendationsRepository = rulesRecommendationsRepository;
        this.productRecommendationsRepository = productRecommendationsRepository;
    }

    @Override
    public UserRecommendationsDTO getAllDynamicRecommendations(UUID user_id) throws UserNotFoundException {

        logger.info("Starting executing all dynamic recommendations for user_id: {}", user_id);

        // Проверка пользователя на наличие в БД
        checkIsUserExists(user_id);

        // Проверка наличия динамических рекомендаций для клиента
        List<ProductRecommendationsDTO> recommendations = checkUserDynamicRecommendations(user_id);

        logger.info("Transferring all found recommendations from List<> to UserRecommendationsDTO for user_id: {}", user_id);
        return new UserRecommendationsDTO(user_id, recommendations);

    }

    @Override
    public String getAllDynamicRulesRecommendationsForTelegramBot(UUID user_id) {

        logger.info("Starting executing all dynamic recommendations fot TelegramBot for user_id: {}", user_id);

        // Проверка наличия динамических рекомендаций для клиента
        List<ProductRecommendationsDTO> recommendations = checkUserDynamicRecommendations(user_id);

        // Преобразование динамической рекомендации в строку для Telegram Bot
        StringBuilder sb = new StringBuilder();

        // Возврат результата в виде строки
        for (ProductRecommendationsDTO recommendation : recommendations) {
            sb.append("*Название продукта:* ").append(recommendation.getProduct_name()).append("\n\n");
            sb.append("*Описание продукта:* \n").append(recommendation.getProduct_text()).append("\n\n\n");
        }
        logger.info("Forwarding all dynamic recommendations in String for TelegramBot for user_id: {}", user_id);
        return sb.toString();
    }

    // Метод поиска клиента в БД
    private boolean checkIsUserExists(UUID user_id) throws UserNotFoundException, NullPointerException {

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
        return true;
    }

    // Метод поиска динамической рекомендации для клиента по БД
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

package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.component.FixedRecommendationsRulesSet;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.FixedRecommendationsRepository;
import pro.sky.recommendation_service.service.UserFixedRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @deprecated
 *
 * Класс для работы со статическими рекомендациями.
 * <p>
 *     Новый функционал см.{@link UserDynamicRecommendationsServiceImpl}
 *
 */
@Service
public class UserFixedRecommendationsServiceImpl implements UserFixedRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(UserFixedRecommendationsServiceImpl.class);

    private final FixedRecommendationsRulesSet[] fixedRecommendationsRulesSets;
    private final FixedRecommendationsRepository fixedRecommendationsRepository;

    @Autowired
    public UserFixedRecommendationsServiceImpl(FixedRecommendationsRulesSet[] fixedRecommendationsRulesSets,
                                               FixedRecommendationsRepository fixedRecommendationsRepository) {
        this.fixedRecommendationsRulesSets = fixedRecommendationsRulesSets;
        this.fixedRecommendationsRepository = fixedRecommendationsRepository;
    }

    /**
     * @deprecated
     * Метод для проверки возможности рекомендовать пользователю продукт,
     * который добавлен в БД путем получения данных из отдельного класса
     *
     * @param user_id идентификатор пользователя в БД
     * @return объект DTO ({@link UserRecommendationsDTO}) хранящий список всех
     *      доступных пользователю рекомендаций
     * @throws UserNotFoundException если пользователь не найден в БД
     */
    @Override
    public UserRecommendationsDTO getAllFixedRecommendations(UUID user_id) throws UserNotFoundException {

        logger.info("Starting checking user in database for user_id: {}", user_id);

        if (fixedRecommendationsRepository.isUserExists(user_id)) {

            logger.info("Starting getting in List<> all recommendations for user_id: {}", user_id);
            List<ProductRecommendationsDTO> recommendations = new ArrayList<>();

            for (FixedRecommendationsRulesSet rule : fixedRecommendationsRulesSets) {
                rule.checkRecommendation(user_id)
                        .ifPresent(recommendations::addAll);
                logger.info("Adding result of getting recommendation to List<> for user_id: {}", user_id);
            }
            logger.info("Transferring all found recommendations from List<> to UserRecommendationsDTO for user_id: {}", user_id);
            return new UserRecommendationsDTO(user_id, recommendations);
        }

        logger.error("Error checking user in database for user_id: {}", user_id);
        throw new UserNotFoundException("User not found in database");
    }

}

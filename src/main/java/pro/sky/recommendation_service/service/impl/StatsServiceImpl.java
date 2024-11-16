package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.entity.Stats;
import pro.sky.recommendation_service.repository.DynamicJPARecommendationsRepository;
import pro.sky.recommendation_service.service.StatsService;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, реализующий работу со счетчиком рекомендаций
 * <p>
 *     Позволяет {@link StatsServiceImpl#incrementStatsCount увеличивать} счетчик
 *     и {@link StatsServiceImpl#getAllStatsCount() получать} список рекомендации / счетчик
 */
@Service
public class StatsServiceImpl implements StatsService {

    private final Logger logger = LoggerFactory.getLogger(StatsServiceImpl.class);

    private final DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository;

    public StatsServiceImpl(DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository) {
        this.dynamicJPARecommendationsRepository = dynamicJPARecommendationsRepository;
    }

    /**
     * Метод позволяет увеличивать счетчик успешной выдачи рекомендации пользователю.
     *
     * @param recommendationsId ID динамической рекомендации ({@link Recommendations})
     */
    @Override
    public void incrementStatsCount(UUID recommendationsId) {

        logger.info("Starting incrementing stats count for dynamic recommendations: {}", recommendationsId);

        Optional<Recommendations> foundRecommendations = dynamicJPARecommendationsRepository.findById(recommendationsId);

        if (foundRecommendations.isPresent()) {
            Recommendations recommendations = foundRecommendations.get();
            Stats stats = recommendations.getStats();

            if (stats == null) {
                stats = new Stats(1);
                stats.setRecommendations(recommendations);
                recommendations.setStats(stats);
            } else {
                stats.setCount(stats.getCount() + 1);
            }

            dynamicJPARecommendationsRepository.save(recommendations);

            logger.info("Successfully added increment stats count for dynamic recommendations: {}", recommendationsId);
        } else {
            logger.warn("Recommendations with ID {} not found.", recommendationsId);
        }
    }

    /**
     * Метод позволяет получить список всех динамических рекомендаций
     * вместе со счетчиком, который показывает, сколько раз данная рекомендация сработала.
     *
     * @return List - обработанный список данных по количеству запросов к каждой динамической рекомендации
     */
    @Override
    public List<Map<String, ? extends Serializable>> getAllStatsCount() {

        logger.info("Getting all dynamic recommendations stats count");
        List<Object[]> statsList = dynamicJPARecommendationsRepository.findAllStats();

        List<Map<String, ? extends Serializable>> responseList = statsList
                .stream()
                .map(stats -> {
                    Integer count = (Integer) stats[0];
                    UUID recommendationsId = (UUID) stats[1];
                    return Map.of("recommendations_id", recommendationsId, "count", count);
                })
                .collect(Collectors.toList());

        logger.info("Successfully got {} dynamic recommendations stats count", responseList.size());
        return responseList;
    }

}

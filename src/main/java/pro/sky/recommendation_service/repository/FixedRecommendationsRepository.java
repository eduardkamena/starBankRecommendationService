package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с фиксированными рекомендациями через JDBC.
 * <p>
 * Используется для выполнения SQL-запросов к базе данных.
 */
@Repository
public class FixedRecommendationsRepository {

    private final Logger logger = LoggerFactory.getLogger(FixedRecommendationsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FixedRecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение суммы транзакций для продукта и типа транзакции.
     *
     * @param userId          идентификатор пользователя
     * @param productsType    тип продукта
     * @param transactionType тип транзакции
     * @return сумма транзакций
     */
    @Cacheable(cacheNames = "fixedRecommendations", key = "#root.methodName + #userId.toString() + #productsType + #transactionType")
    public int getTransactionAmount(UUID userId, String productsType, String transactionType) {
        String sql = "SELECT SUM(t.AMOUNT) AS transactions_amount " +
                "           FROM TRANSACTIONS t " +
                "           INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "           WHERE p.TYPE = '" + productsType + "' " +
                "           AND t.TYPE = '" + transactionType + "' " +
                "           AND t.USER_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        logger.info("Executing a SQL query for user transactions amount " +
                "for userId: {} with product type: {} and transaction type: {}", userId, productsType, transactionType);
        return result != null ? result : 0;
    }

    /**
     * Проверка наличия продукта у пользователя.
     *
     * @param userId       идентификатор пользователя
     * @param productsType тип продукта
     * @return true, если продукт существует, иначе false
     */
    @Cacheable(cacheNames = "fixedRecommendations", key = "#root.methodName + #userId.toString() + #productsType")
    public boolean isProductExists(UUID userId, String productsType) {
        String sql = "SELECT COUNT(*) " +
                "           FROM TRANSACTIONS t " +
                "           INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "           WHERE p.TYPE = '" + productsType + "' " +
                "           AND t.USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        logger.info("Executing a SQL query for the presence of a user product in the database " +
                "for userId: {} and product type: {}", userId, productsType);
        return count != null && count > 0;
    }

    /**
     * Проверка наличия пользователя в базе данных.
     *
     * @param userId идентификатор пользователя
     * @return true, если пользователь существует, иначе false
     */
    @Caching(cacheable = {
            @Cacheable(cacheNames = "dynamicRecommendations", key = "#root.methodName + #userId.toString()"),
            @Cacheable(cacheNames = "fixedRecommendations", key = "#root.methodName + #userId.toString()")
    })
    public boolean isUserExists(UUID userId) {
        String sql = "SELECT COUNT(*) FROM USERS u WHERE u.ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        logger.info("Executing a SQL query \"isUserExists\" in the database for userId: {}", userId);
        return count != null && count > 0;
    }

}

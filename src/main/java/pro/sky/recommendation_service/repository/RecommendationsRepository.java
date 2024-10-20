package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final Logger logger = LoggerFactory.getLogger(RecommendationsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Join SQL запрос в БД на сумму транзакций пополнения или списания (transactionType) по продукту (productsType)
    public int getTransactionAmount(UUID user_id, String productsType, String transactionType) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT SUM(t.AMOUNT) AS transactions_amount " +
                        "FROM TRANSACTIONS t " +
                        "INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                        "WHERE p.TYPE = '" + productsType + "' " +
                        "AND t.TYPE = '" + transactionType + "' " +
                        "AND t.USER_ID = ?",
                Integer.class,
                user_id);
        logger.info("Executing a SQL query for user transactions amount " +
                "for user_id: {} with product type: {} and transaction type: {}", user_id, productsType, transactionType);
        return result != null ? result : 0;
    }

    // Запрос в БД на наличие/отсутствия продукта у пользователя
    public boolean isProductExists(UUID user_id, String productsType) {
        String sql = "SELECT COUNT(*) " +
                "FROM TRANSACTIONS t " +
                "INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE p.TYPE = '" + productsType + "' " +
                "AND t.USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, user_id);
        logger.info("Executing a SQL query for the presence of a user product in the database " +
                "for user_id: {} and product type: {}", user_id, productsType);
        return count != null && count > 0;
    }

    // Запрос в БД на наличие/отсутствия пользователя
    public boolean isUserExists(UUID user_id) {
        String sql = "SELECT COUNT(*) " +
                "FROM USERS u " +
                "WHERE u.ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, user_id);
        logger.info("Executing a SQL query for the presence of a user in the database for user_id: {}", user_id);
        return count != null && count > 0;
    }

}

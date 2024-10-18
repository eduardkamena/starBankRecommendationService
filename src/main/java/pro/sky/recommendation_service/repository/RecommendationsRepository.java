package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Тестовый SQL запрос в БД через JdbcTemplate
    public int getRandomTransactionAmount(UUID user_id, String transactionType, String productsType) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT SUM(t.AMOUNT) AS transactions_amount " +
                        "FROM TRANSACTIONS t " +
                        "INNER JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                        "WHERE t.TYPE = '" + transactionType + "' " +
                        "AND p.TYPE = '" + productsType + "' " +
                        "AND t.USER_ID = ?",
                Integer.class,
                user_id);
        return result != null ? result : 0;
    }

}

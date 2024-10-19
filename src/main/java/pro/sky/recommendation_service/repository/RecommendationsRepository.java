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
        return count != null && count > 0;
    }

}

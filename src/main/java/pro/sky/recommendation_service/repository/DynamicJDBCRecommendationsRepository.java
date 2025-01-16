package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с динамическими рекомендациями через JDBC.
 * <p>
 * Используется для выполнения SQL-запросов к базе данных.
 */
@Repository
public class DynamicJDBCRecommendationsRepository {

    private final Logger logger = LoggerFactory.getLogger(DynamicJDBCRecommendationsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DynamicJDBCRecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Метод проверки нахождения пользователя в БД.
     * <p>
     * Данный метод проверяет, находится ли пользователь в БД/
     * <br><b>Note:</b> БД в соотв. с конфигурацией
     * {@link pro.sky.recommendation_service.configuration.DataSourceConfig#recommendationsJdbcTemplate DataSourceConfig#recommendationsJdbcTemplate}
     * </p>
     *
     * @param userId ID пользователя
     * @return true или false если пользователь найден или отсутствует
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

    /**
     * Метод проверки пользователя.
     * <p>
     * Метод позволяет проверить, является ли пользователь,
     * пользователем продукта.
     * <br>Пользователь использует продукт, если есть хотя бы 1 транзакция.
     * </p>
     *
     * @param userId    ID пользователя
     * @param arguments список передаваемых аргументов правила для сравнения.
     *                  Доступные продукты для проверки пользователя:
     *                  <br>{@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.TransactionProductTypes}
     * @return true или false
     */
    @Cacheable(cacheNames = "dynamicRecommendations", key = "#root.methodName + #userId.toString() + #arguments.get(0)")
    public boolean isUserOf(UUID userId, List<String> arguments) {
        String sql = "SELECT " +
                "           CASE " +
                "               WHEN " +
                "                   (SELECT COUNT(*) " +
                "                   FROM TRANSACTIONS t " +
                "                   INNER JOIN PRODUCTS p " +
                "                   ON t.PRODUCT_ID = p.ID " +
                "                   WHERE t.USER_ID = ? " +
                "                   AND p.TYPE = '" + arguments.get(0) + "') " +
                "                   >= 1 " +
                "               THEN 'true' " +
                "               ELSE 'false' " +
                "           END AS result";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId);

        logger.info("Executing a SQL query \"isUserOf\" " +
                "for userId: {} and product: {}", userId, arguments.get(0));
        return Boolean.TRUE.equals(result);
    }

    /**
     * Метод проверки пользователя.
     * <p>
     * Метод позволяет проверить, является ли пользователь,
     * активным пользователем продукта.
     * <br>Пользователь является активным, если есть хотя бы 5 транзакций по данному продукту.
     * </p>
     *
     * @param userId    ID пользователя
     * @param arguments список передаваемых аргументов правила для сравнения.
     *                  Доступные продукты для проверки пользователя:
     *                  {@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.TransactionProductTypes TransactionProductTypes}
     * @return true или false
     */
    @Cacheable(cacheNames = "dynamicRecommendations", key = "#root.methodName + #userId.toString() + #arguments.get(0)")
    public boolean isActiveUserOf(UUID userId, List<String> arguments) {
        String sql = "SELECT " +
                "           CASE " +
                "               WHEN " +
                "                   (SELECT COUNT(*) " +
                "                   FROM TRANSACTIONS t " +
                "                   INNER JOIN PRODUCTS p " +
                "                   ON t.PRODUCT_ID = p.ID " +
                "                   WHERE t.USER_ID = ? " +
                "                   AND p.TYPE = '" + arguments.get(0) + "') " +
                "                   >= 5 " +
                "               THEN 'true' " +
                "               ELSE 'false' " +
                "           END AS result";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId);

        logger.info("Executing a SQL query \"isActiveUserOf\" " +
                "for userId: {} and product: {}", userId, arguments.get(0));
        return Boolean.TRUE.equals(result);
    }

    /**
     * Метод проверки возможности рекомендации для пользователя.
     * <p>
     * Метод позволяет сравнить сумму всех транзакций (deposit/withdraw) по продукту с некой постоянной
     * </p>
     *
     * @param userId    ID пользователя
     * @param arguments список передаваемых аргументов правила для сравнения.
     *                  Возможные значения аргументов:
     *                  <ul>
     *                      <li>возможные операторы сравнения
     *                      {@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.ComparisonOperators ComparisonOperators}
     *                      <li>возможные продукты и типы транзакций
     *                      {@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.TransactionProductTypes TransactionProductTypes}
     *                      <li>возможные постоянные
     *                      {@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.SumCompare SumCompare}
     *                  </ul>
     * @return true или false
     */
    @Cacheable(cacheNames = "dynamicRecommendations",
            key = "#root.methodName + #userId.toString() + #arguments.get(0) + #arguments.get(1) + #arguments.get(2) + #arguments.get(3)")
    public boolean isTransactionSumCompare(UUID userId, List<String> arguments) {
        String sql = "SELECT " +
                "           CASE " +
                "               WHEN " +
                "                   (SELECT SUM(AMOUNT) " +
                "                   FROM TRANSACTIONS " +
                "                   WHERE PRODUCT_ID IN" +
                "                   (SELECT ID " +
                "                   FROM PRODUCTS " +
                "                   WHERE TYPE = '" + arguments.get(0) + "')" +
                "                   AND TYPE = '" + arguments.get(1) + "' " +
                "                   AND USER_ID = ?) " + arguments.get(2) + " " + arguments.get(3) + " " +
                "               THEN 'true' " +
                "               ELSE 'false' " +
                "           END AS result";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId);

        logger.info("Executing a SQL query \"isTransactionSumCompare\" " +
                "for userId: {} and product: {}", userId, arguments.get(0));
        return Boolean.TRUE.equals(result);
    }

    /**
     * Метод проверки возможности рекомендации для пользователя.
     * <p>
     * Метод позволяет сравнить сумму всех транзакций (deposit)
     * с суммой всех транзакций (withdraw) по продукту
     * </p>
     *
     * @param userId    ID пользователя
     * @param arguments список передаваемых аргументов правила для сравнения.
     *                  Возможные значения аргументов:
     *                  <ul>
     *                      <li>возможные операторы сравнения
     *                      {@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.ComparisonOperators ComparisonOperators}
     *                      <li>возможные продукты и типы транзакций
     *                      {@link pro.sky.recommendation_service.enums.rulesArgumentsENUM.TransactionProductTypes TransactionProductTypes}
     *                  </ul>
     * @return true или false
     */
    @Cacheable(cacheNames = "dynamicRecommendations",
            key = "#root.methodName + #userId.toString() + #arguments.get(0) + #arguments.get(1)")
    public boolean isTransactionSumCompareDepositWithdraw(UUID userId, List<String> arguments) {
        String sql = "SELECT " +
                "           CASE " +
                "               WHEN " +
                "                   (SELECT SUM(t.AMOUNT) " +
                "                   FROM TRANSACTIONS t" +
                "                   JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID" +
                "                   WHERE p.TYPE = '" + arguments.get(0) + "' " +
                "                   AND t.TYPE = 'DEPOSIT' " +
                "                   AND t.USER_ID = ?) " + arguments.get(1) +
                "                   (SELECT SUM(t.AMOUNT) " +
                "                   FROM TRANSACTIONS t" +
                "                   JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID" +
                "                   WHERE p.TYPE = '" + arguments.get(0) + "' " +
                "                   AND t.TYPE = 'WITHDRAW' " +
                "                   AND t.USER_ID = ?) " +
                "               THEN 'true' " +
                "               ELSE 'false' " +
                "           END AS result";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId, userId);

        logger.info("Executing a SQL query \"isTransactionSumCompareDepositWithdraw\" " +
                "for userId: {} and product: {}", userId, arguments.get(0));
        return Boolean.TRUE.equals(result);
    }

}

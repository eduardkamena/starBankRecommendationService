package pro.sky.recommendation_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Загружаем схему и тестовые данные
class DynamicJDBCRecommendationsRepositoryH2Test {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private DynamicJDBCRecommendationsRepository dynamicJDBCRecommendationsRepository;

    @BeforeEach
    void setUp() {
        dynamicJDBCRecommendationsRepository = new DynamicJDBCRecommendationsRepository(jdbcTemplate);
    }

    @Test
    void shouldReturnTrueIfUserExists() {
        // given
        UUID existingUserId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        UUID nonExistingUserId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        // when
        boolean resultExists = dynamicJDBCRecommendationsRepository.isUserExists(existingUserId);
        boolean resultNotExists = dynamicJDBCRecommendationsRepository.isUserExists(nonExistingUserId);

        // then
        assertTrue(resultExists);
        assertFalse(resultNotExists);
    }

    @Test
    void shouldReturnTrueIfUserOf() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("INVEST"); // Тип продукта

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isUserOf(userId, arguments);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfNotUserOf() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("CREDIT"); // Тип продукта, с которым нет транзакций

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isUserOf(userId, arguments);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfActiveUserOf() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("INVEST"); // Тип продукта

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isActiveUserOf(userId, arguments);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfNotActiveUserOf() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("DEBIT"); // Тип продукта, с которым меньше 5 транзакций

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isActiveUserOf(userId, arguments);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfTransactionSumCompare() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("INVEST", "DEPOSIT", ">", "1000"); // Продукт, тип транзакции, оператор, значение

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isTransactionSumCompare(userId, arguments);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfTransactionSumCompare() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("INVEST", "DEPOSIT", "<", "1000"); // Продукт, тип транзакции, оператор, значение

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isTransactionSumCompare(userId, arguments);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfDepositMoreThanWithdraw() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("INVEST", ">"); // Продукт, оператор

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isTransactionSumCompareDepositWithdraw(userId, arguments);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfDepositLessThanWithdraw() {
        // given
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> arguments = List.of("INVEST", "<"); // Продукт, оператор

        // when
        boolean result = dynamicJDBCRecommendationsRepository.isTransactionSumCompareDepositWithdraw(userId, arguments);

        // then
        assertFalse(result);
    }

}

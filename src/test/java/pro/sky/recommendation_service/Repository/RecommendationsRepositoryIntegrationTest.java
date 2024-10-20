package pro.sky.recommendation_service.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pro.sky.recommendation_service.repository.RecommendationsRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Sql({"/test-data.sql"})
class RecommendationsRepositoryIntegrationTest {

    @Autowired
    private RecommendationsRepository recommendationsRepository;

    @Test
    void testGetTransactionAmount() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        int amount = recommendationsRepository.getTransactionAmount(userId, "INVESTMENT", "DEPOSIT");
        assertEquals(5000, amount);
    }

    @Test
    void testIsProductExists() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");
        boolean exists = recommendationsRepository.isProductExists(userId, "DEBIT");

        assertTrue(exists);
    }
    @Test
    void testIsUserExists() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        boolean userExists = recommendationsRepository.isUserExists(userId);

        assertTrue(userExists);
    }

    @Test
    void testIsUserNotExists() {
        UUID userId = UUID.randomUUID();

        boolean userExists = recommendationsRepository.isUserExists(userId);

        assertFalse(userExists);
    }
}

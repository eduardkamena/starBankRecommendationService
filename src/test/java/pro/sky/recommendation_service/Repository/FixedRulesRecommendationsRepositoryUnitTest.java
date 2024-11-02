package pro.sky.recommendation_service.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.recommendation_service.repository.FixedRulesRecommendationsRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FixedRulesRecommendationsRepositoryUnitTest {

    @Autowired
    private FixedRulesRecommendationsRepository fixedRulesRecommendationsRepository;

    private final UUID USER_ID = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
    private final String PRODUCTS_TYPE = "DEBIT";
    private final String TRANSACTION_TYPE = "DEPOSIT";

    @BeforeEach
    public void setUp() {
    }

    @Test
    void shouldGetTransactionAmount() {
        // given
        // when
        int amount = fixedRulesRecommendationsRepository.getTransactionAmount(USER_ID, PRODUCTS_TYPE, TRANSACTION_TYPE);

        // then
        assertEquals(110_813, amount);
    }

    @Test
    void shouldExpectProductIsExists() {
        // given
        // when
        boolean exists = fixedRulesRecommendationsRepository.isProductExists(USER_ID, PRODUCTS_TYPE);

        // then
        assertTrue(exists);
    }

    @Test
    void shouldExpectUserIsExists() {
        // given
        // when
        boolean userExists = fixedRulesRecommendationsRepository.isUserExists(USER_ID);

        // then
        assertTrue(userExists);
    }

    @Test
    void shouldExpectUserIsNotExists() {
        // given
        UUID userRandomId = UUID.randomUUID();

        // when
        boolean userExists = fixedRulesRecommendationsRepository.isUserExists(userRandomId);

        // then
        assertFalse(userExists);
    }

}

package pro.sky.recommendation_service.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.recommendation_service.repository.RecommendationsRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationsRepositoryUnitTest {

    @Autowired
    private RecommendationsRepository recommendationsRepository;

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
        int amount = recommendationsRepository.getTransactionAmount(USER_ID, PRODUCTS_TYPE, TRANSACTION_TYPE);

        // then
        assertEquals(110_813, amount);
    }

    @Test
    void shouldExpectProductIsExists() {
        // given
        // when
        boolean exists = recommendationsRepository.isProductExists(USER_ID, PRODUCTS_TYPE);

        // then
        assertTrue(exists);
    }

    @Test
    void shouldExpectUserIsExists() {
        // given
        // when
        boolean userExists = recommendationsRepository.isUserExists(USER_ID);

        // then
        assertTrue(userExists);
    }

    @Test
    void shouldExpectUserIsNotExists() {
        // given
        UUID userRandomId = UUID.randomUUID();

        // when
        boolean userExists = recommendationsRepository.isUserExists(userRandomId);

        // then
        assertFalse(userExists);
    }

}

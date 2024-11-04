package pro.sky.recommendation_service.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import pro.sky.recommendation_service.repository.FixedRecommendationsRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class FixedRecommendationsRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private FixedRecommendationsRepository fixedRecommendationsRepository;

    private UUID userId;
    private String productsType;
    private String transactionType;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        productsType = "SAVINGS";
        transactionType = "DEPOSIT";
    }

    @Test
    public void testGetTransactionAmount_ShouldReturnAmount() {

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
                .thenReturn(500);

        int result = fixedRecommendationsRepository.getTransactionAmount(userId, productsType, transactionType);

        assertEquals(500, result);

        verify(jdbcTemplate, times(1))
                .queryForObject(anyString(), eq(Integer.class), eq(userId));
    }

    @Test
    public void testGetTransactionAmount_ShouldReturnZeroIfNull() {
        // Mock the database result to return null
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
                .thenReturn(null);

        int result = fixedRecommendationsRepository.getTransactionAmount(userId, productsType, transactionType);

        assertEquals(0, result);
    }

    @Test
    public void testIsProductExists_ShouldReturnTrue() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
                .thenReturn(1);

        boolean result = fixedRecommendationsRepository.isProductExists(userId, productsType);

        assertTrue(result);
    }

    @Test
    public void testIsUserExists_ShouldReturnTrue() {

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
                .thenReturn(1);
        boolean result = fixedRecommendationsRepository.isUserExists(userId);
        assertTrue(result);
    }

}
package pro.sky.recommendation_service.Service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.recommendation_service.component.FixedRecommendationRuleSet;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.TransactionsRepository;
import pro.sky.recommendation_service.service.impl.UserRecommendationsServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserRecommendationsServiceImplTests {

    private UserRecommendationsServiceImpl recommendationService;

    @Mock
    private FixedRecommendationRuleSet ruleSetMock;

    @Mock
    private TransactionsRepository transactionsRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recommendationService = new UserRecommendationsServiceImpl(new FixedRecommendationRuleSet[]{ruleSetMock}, transactionsRepositoryMock);
    }

    @Test
    void testGetAllRecommendations_WithRecommendations() {
        UUID userId = UUID.randomUUID();
        ProductRecommendationsDTO recommendation = new ProductRecommendationsDTO(
                "Инвестиции", UUID.randomUUID(), "Инвестируйте 500");

        List<ProductRecommendationsDTO> recommendationsList = Collections.singletonList(recommendation);

        when(transactionsRepositoryMock.isUserExists(userId)).thenReturn(true);

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.of(recommendationsList));

        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(1, result.getRecommendations().size());
        assertEquals(userId, result.getUser_id());
        assertEquals("Инвестиции", result.getRecommendations().get(0).getProduct_name());
    }

    @Test
    void testGetAllRecommendations_WithoutRecommendations() {
        UUID userId = UUID.randomUUID();

        when(transactionsRepositoryMock.isUserExists(userId)).thenReturn(true);

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.empty());

        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(0, result.getRecommendations().size());
        assertEquals(userId, result.getUser_id());
    }

    @Test
    void testGetAllRecommendations_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(transactionsRepositoryMock.isUserExists(userId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            recommendationService.getAllRecommendations(userId);
        });

        assertEquals("User not found in database", exception.getMessage());
    }
}
package pro.sky.recommendation_service.Service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.recommendation_service.component.FixedRecommendationsRulesSet;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.FixedRecommendationsRepository;
import pro.sky.recommendation_service.service.impl.UserFixedRecommendationsServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDynamicRulesRecommendationsServiceImplTests {

    private UserFixedRecommendationsServiceImpl recommendationService;

    @Mock
    private FixedRecommendationsRulesSet ruleSetMock;

    @Mock
    private FixedRecommendationsRepository fixedRecommendationsRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recommendationService = new UserFixedRecommendationsServiceImpl(new FixedRecommendationsRulesSet[]{ruleSetMock}, fixedRecommendationsRepositoryMock);
    }

    @Test
    void testGetAllRecommendations_WithRecommendations() {
        UUID userId = UUID.randomUUID();
        ProductRecommendationsDTO recommendation = new ProductRecommendationsDTO(
                "Инвестиции", UUID.randomUUID(), "Инвестируйте 500");

        List<ProductRecommendationsDTO> recommendationsList = Collections.singletonList(recommendation);

        when(fixedRecommendationsRepositoryMock.isUserExists(userId)).thenReturn(true);

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.of(recommendationsList));

        UserRecommendationsDTO result = recommendationService.getAllFixedRecommendations(userId);

        assertEquals(1, result.recommendations().size());
        assertEquals(userId, result.user_id());
        assertEquals("Инвестиции", result.recommendations().get(0).getProduct_name());
    }

    @Test
    void testGetAllRecommendations_WithoutRecommendations() {
        UUID userId = UUID.randomUUID();

        when(fixedRecommendationsRepositoryMock.isUserExists(userId)).thenReturn(true);

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.empty());

        UserRecommendationsDTO result = recommendationService.getAllFixedRecommendations(userId);

        assertEquals(0, result.recommendations().size());
        assertEquals(userId, result.user_id());
    }

    @Test
    void testGetAllRecommendations_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(fixedRecommendationsRepositoryMock.isUserExists(userId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            recommendationService.getAllFixedRecommendations(userId);
        });

        assertEquals("User not found in database", exception.getMessage());
    }
}
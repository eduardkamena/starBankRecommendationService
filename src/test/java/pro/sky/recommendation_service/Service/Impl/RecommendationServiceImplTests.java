package pro.sky.recommendation_service.Service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.service.impl.RecommendationServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RecommendationServiceImplTests {

    private RecommendationServiceImpl recommendationService;

    @Mock
    private RecommendationRuleSet ruleSetMock;

    @Mock
    private RecommendationsRepository recommendationsRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recommendationService = new RecommendationServiceImpl(new RecommendationRuleSet[]{ruleSetMock}, recommendationsRepositoryMock);
    }

    @Test
    void testGetAllRecommendations_WithRecommendations() {
        UUID userId = UUID.randomUUID();
        RecommendationDTO recommendation = new RecommendationDTO("Инвестиции", UUID.randomUUID(), "Инвестируйте 500");

        when(recommendationsRepositoryMock.isUserExists(userId)).thenReturn(true);

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.of(recommendation));

        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(1, result.getRecommendations().size());
        assertEquals(userId, result.getUser_id());
        assertEquals("Инвестиции", result.getRecommendations().get(0).getName());
    }

    @Test
    void testGetAllRecommendations_WithoutRecommendations() {
        UUID userId = UUID.randomUUID();

        when(recommendationsRepositoryMock.isUserExists(userId)).thenReturn(true);

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.empty());

        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(0, result.getRecommendations().size());
        assertEquals(userId, result.getUser_id());
    }

    @Test
    void testGetAllRecommendations_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(recommendationsRepositoryMock.isUserExists(userId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            recommendationService.getAllRecommendations(userId);
        });

        assertEquals("User not found in database", exception.getMessage());
    }
}
package pro.sky.recommendation_service.Service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.service.impl.RecommendationServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RecommendationServiceImplTest {

    private pro.sky.recommendation_service.service.impl.RecommendationServiceImpl recommendationService;

    @Mock
    private RecommendationRuleSet ruleSetMock;

    @Mock
    private RecommendationsRepository recommendationsRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recommendationService = new pro.sky.recommendation_service.service.impl.RecommendationServiceImpl
                (new RecommendationRuleSet[]{ruleSetMock},
                        recommendationsRepositoryMock);
    }

    @Test
    void testGetAllRecommendations_WithRecommendations() {
        UUID userId = UUID.randomUUID();
        //UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        RecommendationDTO recommendation = new RecommendationDTO("Investment", UUID.randomUUID(), "Invest 500");

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.of(recommendation));

        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(1, result.getRecommendations().size());
        assertEquals(userId, result.getUser_id());
        assertEquals("Investment", result.getRecommendations().get(0).getName());
    }

    @Test
    void testGetAllRecommendationsWithoutRecommendations() {
        UUID userId = UUID.randomUUID();

        when(ruleSetMock.checkRecommendation(userId))
                .thenReturn(Optional.empty());

        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(0, result.getRecommendations().size());
    }

    @Test
    public void getAllRecommendationsTest() {
        RecommendationRuleSet mockRule = Mockito.mock(RecommendationRuleSet.class);
        Mockito.when(mockRule.checkRecommendation(any(UUID.class))).thenReturn(Optional.empty());

        RecommendationServiceImpl recommendationService = new RecommendationServiceImpl(new RecommendationRuleSet[]{mockRule}, recommendationsRepositoryMock);
        UUID userId = UUID.randomUUID();
        UserRecommendationsDTO result = recommendationService.getAllRecommendations(userId);

        assertEquals(userId, result.getUser_id());
        assertEquals(0, result.getRecommendations().size());
    }
}
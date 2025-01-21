package pro.sky.recommendation_service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.entity.Rules;
import pro.sky.recommendation_service.enums.RulesQueryENUM;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.DynamicJDBCRecommendationsRepository;
import pro.sky.recommendation_service.repository.DynamicJPARecommendationsRepository;
import pro.sky.recommendation_service.repository.ProductRecommendationsRepository;
import pro.sky.recommendation_service.repository.RulesRecommendationsRepository;
import pro.sky.recommendation_service.service.StatsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDynamicRecommendationsServiceImplH2Test {

    @Mock
    private DynamicJDBCRecommendationsRepository dynamicJDBCRecommendationsRepository;

    @Mock
    private DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository;

    @Mock
    private RulesRecommendationsRepository rulesRecommendationsRepository;

    @Mock
    private ProductRecommendationsRepository productRecommendationsRepository;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private UserDynamicRecommendationsServiceImpl userDynamicRecommendationsService;

    private UUID userId;
    private UUID recommendationId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        recommendationId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
        productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    }

    @Test
    void shouldGetAllDynamicRecommendationsIfUserExists() throws UserNotFoundException {
        // given
        when(dynamicJDBCRecommendationsRepository.isUserExists(userId)).thenReturn(true);
        when(dynamicJPARecommendationsRepository.findAllRecommendationsIDs()).thenReturn(List.of(recommendationId));

        Rules rule = new Rules();
        rule.setQuery(RulesQueryENUM.USER_OF);
        rule.setArguments(List.of("INVEST"));
        rule.setNegate(false);

        when(rulesRecommendationsRepository.findByRecommendationsId(recommendationId)).thenReturn(List.of(rule));

        Recommendations recommendation = new Recommendations();
        recommendation.setProductName("Invest 500");
        recommendation.setProductId(productId);
        recommendation.setProductText("Описание Invest 500");

        when(productRecommendationsRepository.findById(recommendationId)).thenReturn(Optional.of(recommendation));

        // when
        UserRecommendationsDTO result = userDynamicRecommendationsService.getAllDynamicRecommendations(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.userId());
        assertEquals(1, result.recommendations().size());
        verify(statsService, times(1)).incrementStatsCount(recommendationId);
    }

    @Test
    void shouldGetAllDynamicRecommendationsIfUserNotFound() {
        // given & when
        when(dynamicJDBCRecommendationsRepository.isUserExists(userId)).thenReturn(false);

        // then
        assertThrows(UserNotFoundException.class, () -> userDynamicRecommendationsService.getAllDynamicRecommendations(userId));
    }

    @Test
    void shouldGetAllDynamicRulesRecommendationsForTelegramBot() {
        // given
        when(dynamicJPARecommendationsRepository.findAllRecommendationsIDs()).thenReturn(List.of(recommendationId));

        Rules rule = new Rules();
        rule.setQuery(RulesQueryENUM.USER_OF);
        rule.setArguments(List.of("INVEST"));
        rule.setNegate(false);

        when(rulesRecommendationsRepository.findByRecommendationsId(recommendationId)).thenReturn(List.of(rule));

        Recommendations recommendation = new Recommendations();
        recommendation.setProductName("Invest 500");
        recommendation.setProductId(productId);
        recommendation.setProductText("Описание Invest 500");

        when(productRecommendationsRepository.findById(recommendationId)).thenReturn(Optional.of(recommendation));

        // when
        String result = userDynamicRecommendationsService.getAllDynamicRulesRecommendationsForTelegramBot(userId);

        // then
        assertNotNull(result);
        assertTrue(result.contains("Invest 500"));
        assertTrue(result.contains("Описание Invest 500"));
    }

}

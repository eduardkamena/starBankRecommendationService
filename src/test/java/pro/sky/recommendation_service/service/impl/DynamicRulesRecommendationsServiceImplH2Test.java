package pro.sky.recommendation_service.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.recommendation_service.configuration.TelegramBotConfig;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.exception.RuleNotFoundException;
import pro.sky.recommendation_service.listener.TelegramBotUpdatesListener;
import pro.sky.recommendation_service.repository.DynamicJPARecommendationsRepository;
import pro.sky.recommendation_service.service.DynamicRulesRecommendationsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pro.sky.recommendation_service.enums.RulesQueryENUM.USER_OF;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class DynamicRulesRecommendationsServiceImplH2Test {

    @MockBean
    private TelegramBotConfig telegramBotConfig;

    @MockBean
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @MockBean
    private MessageSenderServiceImpl messageSenderServiceImpl;

    @MockBean
    private StatsServiceImpl statsServiceImpl;

    @Autowired
    private DynamicRulesRecommendationsService dynamicRulesRecommendationsService;

    @Autowired
    private DynamicJPARecommendationsRepository dynamicJPARecommendationsRepository;

    @Test
    void shouldCreateDynamicRuleRecommendation() {
        // given
        RecommendationsDTO recommendationsDTO = new RecommendationsDTO();
        recommendationsDTO.setProductName("Invest 500");
        recommendationsDTO.setProductId(UUID.randomUUID());
        recommendationsDTO.setProductText("Описание Invest 500");

        RulesDTO ruleDTO = new RulesDTO();
        ruleDTO.setQuery(USER_OF);
        ruleDTO.setArguments(List.of("INVEST"));
        ruleDTO.setNegate(false);

        recommendationsDTO.setRule(List.of(ruleDTO));

        // when
        Recommendations result = dynamicRulesRecommendationsService.createDynamicRuleRecommendation(recommendationsDTO);

        // then
        assertNotNull(result);
        assertEquals(1, result.getRule().size());
        assertEquals("Invest 500", result.getProductName());
        assertEquals("Описание Invest 500", result.getProductText());
        assertEquals("INVEST", result.getRule().get(0).getArguments().get(0));
    }

    @Test
    void shouldGetDynamicRuleRecommendation() {
        // given
        UUID recommendationId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");

        // when
        Optional<RecommendationsDTO> result = dynamicRulesRecommendationsService.getDynamicRuleRecommendation(recommendationId);

        // then
        assertTrue(result.isPresent());
        assertEquals("Invest 500", result.get().getProductName());
    }

    @Test
    void shouldThrowWhenGetDynamicRuleRecommendationNotFound() {
        // given
        UUID recommendationId = UUID.randomUUID();

        // when & then
        assertThrows(RuleNotFoundException.class, () -> {
            dynamicRulesRecommendationsService.getDynamicRuleRecommendation(recommendationId);
        });
    }

    @Test
    void shouldGetAllDynamicRulesRecommendations() {
        // given & when
        List<RecommendationsDTO> result = dynamicRulesRecommendationsService.getAllDynamicRulesRecommendations();

        // then
        assertFalse(result.isEmpty());
        assertEquals("Invest 500", result.get(0).getProductName());
    }

    @Test
    void shouldDeleteDynamicRuleRecommendation() {
        // given
        UUID recommendationId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");

        // when
        dynamicRulesRecommendationsService.deleteDynamicRuleRecommendation(recommendationId);

        // then
        assertFalse(dynamicJPARecommendationsRepository.existsById(recommendationId));
    }

}

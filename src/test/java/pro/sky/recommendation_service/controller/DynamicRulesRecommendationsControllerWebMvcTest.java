package pro.sky.recommendation_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.exception.RuleNotFoundException;
import pro.sky.recommendation_service.service.DynamicRulesRecommendationsService;
import pro.sky.recommendation_service.service.StatsService;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DynamicRulesRecommendationsController.class)
class DynamicRulesRecommendationsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DynamicRulesRecommendationsService dynamicRulesRecommendationsService;

    @MockBean
    private StatsService statsService;

    @Test
    void shouldCreateDynamicRecommendation() throws Exception {
        // given
        UUID productId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        RecommendationsDTO recommendationsDTO = new RecommendationsDTO();
        recommendationsDTO.setProductName("Test Product");
        recommendationsDTO.setProductId(productId);
        recommendationsDTO.setProductText("Test Description");

        // Создаем объект Recommendations (сущность) для возврата из мока
        Recommendations recommendations = new Recommendations();
        recommendations.setProductName(recommendationsDTO.getProductName());
        recommendations.setProductId(recommendationsDTO.getProductId());
        recommendations.setProductText(recommendationsDTO.getProductText());

        // when
        when(dynamicRulesRecommendationsService.createDynamicRuleRecommendation(recommendationsDTO))
                .thenReturn(recommendations);

        // then
        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Test Product\",\"productId\":\"d4a4d619-9a0c-4fc5-b0cb-76c49409546b\",\"productText\":\"Test Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.productId").value(productId.toString()))
                .andExpect(jsonPath("$.productText").value("Test Description"));
    }

    @Test
    void shouldGetDynamicRecommendation() throws Exception {
        // given
        UUID recommendationId = UUID.randomUUID();
        RecommendationsDTO recommendationsDTO = new RecommendationsDTO();
        recommendationsDTO.setId(recommendationId);
        recommendationsDTO.setProductName("Test Product");

        // when
        when(dynamicRulesRecommendationsService.getDynamicRuleRecommendation(recommendationId))
                .thenReturn(Optional.of(recommendationsDTO));

        // then
        mockMvc.perform(get("/rule/{ruleId}", recommendationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recommendationId.toString()))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    void shouldGetNotFoundDynamicRecommendation() throws Exception {
        // given
        UUID recommendationId = UUID.randomUUID();

        // when
        when(dynamicRulesRecommendationsService.getDynamicRuleRecommendation(recommendationId))
                .thenThrow(new RuleNotFoundException("Rule not found"));

        // then
        mockMvc.perform(get("/rule/{ruleId}", recommendationId))
                .andExpect(status().isNotFound());
    }

}

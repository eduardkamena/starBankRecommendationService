package pro.sky.recommendation_service.Controller;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.recommendation_service.controller.RecommendationsController;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.service.RecommendationService;

import java.util.UUID;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(RecommendationsController.class)
public class RecommendationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    public void getUserRecommendationTest() throws Exception {
        UUID userId = UUID.randomUUID();
        UserRecommendationsDTO mockDTO = new UserRecommendationsDTO(userId, Collections.emptyList());

        Mockito.when(recommendationService.getAllRecommendations(userId)).thenReturn(mockDTO);

        mockMvc.perform(get("/recommendation/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id", is(userId.toString())));
    }
}
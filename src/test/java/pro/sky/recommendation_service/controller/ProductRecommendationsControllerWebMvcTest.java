package pro.sky.recommendation_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.exception.ProductNotFoundException;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRecommendationsController.class)
class ProductRecommendationsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRecommendationsService productRecommendationsService;

    @Test
    void shouldGetProduct() throws Exception {
        // given
        UUID productId = UUID.randomUUID();
        ProductRecommendationsDTO productDTO = new ProductRecommendationsDTO("Test Product", productId, "Test Description");

        // when
        when(productRecommendationsService.getRecommendationProduct(productId))
                .thenReturn(List.of(productDTO));

        // then
        mockMvc.perform(get("/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Test Product"))
                .andExpect(jsonPath("$[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$[0].productText").value("Test Description"));
    }

    @Test
    void shouldGetNotFoundProduct() throws Exception {
        // given
        UUID productId = UUID.randomUUID();

        // when
        when(productRecommendationsService.getRecommendationProduct(productId))
                .thenThrow(new ProductNotFoundException("Product not found"));

        // then
        mockMvc.perform(get("/product/{productId}", productId))
                .andExpect(status().isNotFound());
    }

}

package pro.sky.recommendation_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CacheController.class)
class CacheControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheManager cacheManager;

    @Test
    void shouldClearCaches() throws Exception {
        // when & then
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());
    }

}

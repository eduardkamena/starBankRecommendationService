package pro.sky.recommendation_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuildInfoController.class)
class BuildInfoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildProperties buildProperties;

    @Test
    void shouldGetBuildInfo() throws Exception {
        // given
        when(buildProperties.getVersion()).thenReturn("0.1.0");

        // when & then
        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("0.1.0"));
    }

}

package pro.sky.recommendation_service.Controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.recommendation_service.controller.UserRecommendationsController;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.service.UserRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(UserRecommendationsController.class)
public class UserProductRecommendationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRecommendationsService userRecommendationsService;

    private final String NAME = "Top Saving";
    private final UUID ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private final String TEXT = """
            Откройте свою собственную «Копилку» с нашим банком!
            «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели.
            Больше никаких забытых чеков и потерянных квитанций — всё под контролем!
            Преимущества «Копилки»:
            Накопление средств на конкретные цели.
            Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.
            Прозрачность и контроль.
            Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.
            Безопасность и надежность.
            Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.
            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

    @Test
    public void shouldGetUserRecommendations() throws Exception {
        // given
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        Recommendations recommendations = new Recommendations(NAME, ID, TEXT);
        List<Recommendations> recommendationList = new ArrayList<>();
        recommendationList.add(recommendations);

        UserRecommendationsDTO mockDTO = new UserRecommendationsDTO(userId, recommendationList);

        // when
        Mockito.when(userRecommendationsService.getAllRecommendations(userId)).thenReturn(mockDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId) //send
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.user_id").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations[0].name").value(NAME))
                .andExpect(jsonPath("$.recommendations[0].id").value(ID.toString()))
                .andExpect(jsonPath("$.recommendations[0].text").value(TEXT));
    }

    @Test
    public void shouldGetUserEmptyListRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();
        UserRecommendationsDTO mockDTO = new UserRecommendationsDTO(userId, Collections.emptyList());

        Mockito.when(userRecommendationsService.getAllRecommendations(userId)).thenReturn(mockDTO);

        mockMvc.perform(get("/recommendation/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id", is(userId.toString())));
    }

}

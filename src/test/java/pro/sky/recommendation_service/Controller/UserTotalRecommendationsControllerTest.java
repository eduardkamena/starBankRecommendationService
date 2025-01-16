package pro.sky.recommendation_service.Controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.recommendation_service.controller.UserTotalRecommendationsController;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;
import pro.sky.recommendation_service.service.UserFixedRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(UserTotalRecommendationsController.class)
public class UserTotalRecommendationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFixedRecommendationsService userFixedRecommendationsService;

    @MockBean
    private UserDynamicRecommendationsService userDynamicRecommendationsService;

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
    public void shouldGetFixedUserRecommendations() throws Exception {
        // given
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        ProductRecommendationsDTO recommendations = new ProductRecommendationsDTO(NAME, ID, TEXT);
        List<ProductRecommendationsDTO> recommendationList = new ArrayList<>();
        recommendationList.add(recommendations);

        UserRecommendationsDTO mockDTO = new UserRecommendationsDTO(userId, recommendationList);

        // when
        Mockito.when(userFixedRecommendationsService.getAllFixedRecommendations(userId)).thenReturn(mockDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/fixed/" + userId) //send
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations[0].productName").value(NAME))
                .andExpect(jsonPath("$.recommendations[0].productId").value(ID.toString()))
                .andExpect(jsonPath("$.recommendations[0].productText").value(TEXT));
    }

    @Test
    public void shouldGetUserEmptyListRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();
        UserRecommendationsDTO mockDTO = new UserRecommendationsDTO(userId, Collections.emptyList());

        Mockito.when(userFixedRecommendationsService.getAllFixedRecommendations(userId)).thenReturn(mockDTO);

        mockMvc.perform(get("/recommendation/fixed/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId.toString())));
    }

}

package pro.sky.recommendation_service.Service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.TransactionsRepository;
import pro.sky.recommendation_service.service.UserRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRecommendationsServiceImplUnitTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private RecommendationRuleSet recommendationRuleSet;

    @Mock
    private UserRecommendationsService userRecommendationsService;

    @BeforeEach
    public void setUp() {
    }

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
    void shouldGetAllRecommendations_WithRecommendations() {
        // given
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        Recommendations recommendations = new Recommendations(NAME, ID, TEXT);
        List<Recommendations> recommendationList = new ArrayList<>();
        recommendationList.add(recommendations);

        UserRecommendationsDTO mockDTO = new UserRecommendationsDTO(userId, recommendationList);

        // when
        when(transactionsRepository.isUserExists(userId)).thenReturn(true);
        when(userRecommendationsService.getAllRecommendations(userId)).thenReturn(mockDTO);

        UserRecommendationsDTO result = userRecommendationsService.getAllRecommendations(userId);

        // then
        assertEquals(userId, result.getUser_id());
        assertEquals(1, result.getRecommendations().size());
        assertEquals(NAME, result.getRecommendations().get(0).getName());
        assertEquals(ID, result.getRecommendations().get(0).getId());
        assertEquals(TEXT, result.getRecommendations().get(0).getText());

        verify(userRecommendationsService).getAllRecommendations(userId);
    }

    @Test
    void shouldGetAllRecommendations_WithoutRecommendations() {
        //given
        UUID userId = UUID.randomUUID();
        List<Recommendations> recommendationList = new ArrayList<>();

        // when
        when(recommendationRuleSet.checkRecommendation(userId)).thenReturn(Optional.empty());

        UserRecommendationsDTO result = new UserRecommendationsDTO(userId, recommendationList);

        // then
        assertEquals(0, result.getRecommendations().size());
    }

    @Test
    public void shouldThrowUserNotFoundException() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        when(userRecommendationsService.getAllRecommendations(userId)).thenThrow(UserNotFoundException.class);

        // then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userRecommendationsService.getAllRecommendations(userId));
    }

}

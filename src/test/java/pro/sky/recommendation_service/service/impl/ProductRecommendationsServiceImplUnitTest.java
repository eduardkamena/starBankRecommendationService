package pro.sky.recommendation_service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.exception.ProductNotFoundException;
import pro.sky.recommendation_service.repository.ProductRecommendationsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductRecommendationsServiceImplUnitTest {

    @Mock
    private ProductRecommendationsRepository productRecommendationsRepository;

    @Mock
    private ProductRecommendationsServiceImpl productRecommendationsService;

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
    void shouldGetRecommendationProduct() {
        // given
        ProductRecommendationsDTO recommendations = new ProductRecommendationsDTO(NAME, ID, TEXT);
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{NAME, ID, TEXT});

        List<ProductRecommendationsDTO> mockDTO = new ArrayList<>();
        mockDTO.add(recommendations);

        // when
        when(productRecommendationsRepository.findByProductId(ID))
                .thenReturn(mockResult);
        when(productRecommendationsService.getRecommendationProduct(ID))
                .thenReturn(mockDTO);

        List<ProductRecommendationsDTO> result = productRecommendationsService.getRecommendationProduct(ID);

        // then
        assertFalse(result.isEmpty());
        assertEquals(NAME, result.get(0).getProductName());
        assertEquals(ID, result.get(0).getProductId());
        assertEquals(TEXT, result.get(0).getProductText());
    }

    @Test
    void shouldThrowWhenGetRecommendationProductNotFound() {
        // given
        UUID productId = UUID.randomUUID();

        // when
        when(productRecommendationsService.getRecommendationProduct(productId)).thenThrow(ProductNotFoundException.class);

        // then
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productRecommendationsService.getRecommendationProduct(productId));
    }

}

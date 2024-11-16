package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.exception.AppError;
import pro.sky.recommendation_service.exception.ProductNotFoundException;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с продуктами рекомендаций
 */
@RestController
@RequestMapping(path = "/product")
@Tag(
        name = "Контроллер продукта рекомендации",
        description = "Выполняет действия с продуктом рекомендации")
public class ProductRecommendationsController {

    private final Logger logger = LoggerFactory.getLogger(ProductRecommendationsController.class);

    private final ProductRecommendationsService productRecommendationsService;

    public ProductRecommendationsController(ProductRecommendationsService productRecommendationsService) {
        this.productRecommendationsService = productRecommendationsService;
    }

    /**
     * GET-Метод получения записи продукта из БД
     *
     * @param productId ID продукта
     * @return 200 - JSON представление {@link ProductRecommendationsDTO продукта}
     *      <p>400 - рекомендация не найдена
     */
    @GetMapping(path = "/{productId}")
    @Operation(
            summary = "Получение продукта рекомендации",
            description = "Позволяет получить продукт рекомендации")
    public ResponseEntity<Object> getProduct(
            @Parameter(description = "Идентификатор продукта рекомендации")
            @PathVariable UUID productId) {

        logger.info("Received request for getting recommendation product for productId: {}", productId);

        productId = UUID.fromString(String.valueOf(productId));

        try {
            List<ProductRecommendationsDTO> result = productRecommendationsService.getRecommendationProduct(productId);
            logger.info("Outputting in @Controller recommendation product for productId: {}", productId);
            return ResponseEntity.ok(result);

        } catch (ProductNotFoundException e) {

            logger.error("Error Outputting in @Controller recommendation product for productId: {}", productId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Product with UUID " + productId + " not found in database"));
        }
    }

}

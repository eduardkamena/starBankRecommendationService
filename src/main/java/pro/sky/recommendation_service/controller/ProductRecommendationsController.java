package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.exception.AppError;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.UUID;

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

    @GetMapping(path = "/{product_id}")
    public ResponseEntity<Object> getRecommendation(
            @Parameter(description = "Идентификатор продукта рекомендации")
            @PathVariable UUID product_id) {

        logger.info("Received request for getting recommendation product for product_id: {}", product_id);

        product_id = UUID.fromString(String.valueOf(product_id));

        try {
            List<ProductRecommendationsDTO> result = productRecommendationsService.getRecommendationProduct(product_id);
            logger.info("Outputting recommendation product for product_id: {}", product_id);
            return ResponseEntity.ok(result);

        } catch (EmptyResultDataAccessException e) {

            logger.error("Error Outputting recommendation product for product_id: {}", product_id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Product with UUID " + product_id + " not found in database"));
        }
    }

}

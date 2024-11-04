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
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.AppError;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;
import pro.sky.recommendation_service.service.UserFixedRecommendationsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/recommendation")
@Tag(
        name = "Контроллер рекомендаций продукта(ов) клиенту",
        description = "Выполняет действия с рекомендациями продукта(ов) клиенту")
public class UserTotalRecommendationsController {

    private final Logger logger = LoggerFactory.getLogger(UserTotalRecommendationsController.class);

    private final UserFixedRecommendationsService userFixedRecommendationsService;
    private final UserDynamicRecommendationsService userDynamicRecommendationsService;

    public UserTotalRecommendationsController(UserFixedRecommendationsService userFixedRecommendationsService, UserDynamicRecommendationsService userDynamicRecommendationsService) {
        this.userFixedRecommendationsService = userFixedRecommendationsService;
        this.userDynamicRecommendationsService = userDynamicRecommendationsService;
    }

    @GetMapping(path = "/fixed/{user_id}")
    @Operation(
            summary = "Получение фиксированных рекомендаций продукта(ов)",
            description = "Позволяет получить фиксированные рекомендации продукта(ов) клиенту")
    public ResponseEntity<Object> getUserFixedRecommendations(
            @PathVariable
            @Parameter(description = "Идентификатор клиента") UUID user_id) {
        logger.info("Received request for getting all fixed relevant recommendations for user_id: {}", user_id);

        try {
            UserRecommendationsDTO result = userFixedRecommendationsService.getAllFixedRecommendations(user_id);
            logger.info("Outputting in @Controller all fixed relevant recommendations for user_id: {}", user_id);
            return ResponseEntity.ok(result);

        } catch (UserNotFoundException e) {

            logger.error("Error Outputting in @Controller all fixed relevant recommendations for user_id: {}", user_id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with UUID " + user_id + " not found in database"));
        }
    }

    @GetMapping(path = "/dynamic/{user_id}")
    @Operation(
            summary = "Получение фиксированных рекомендаций продукта(ов)",
            description = "Позволяет получить фиксированные рекомендации продукта(ов) клиенту")
    public ResponseEntity<Object> getUserDynamicRecommendations(
            @PathVariable
            @Parameter(description = "Идентификатор клиента") UUID user_id) {
        logger.info("Received request for getting all dynamic relevant recommendations for user_id: {}", user_id);

        try {
            UserRecommendationsDTO result = userDynamicRecommendationsService.getAllDynamicRecommendations(user_id);
            logger.info("Outputting in @Controller all dynamic relevant recommendations for user_id: {}", user_id);
            return ResponseEntity.ok(result);

        } catch (UserNotFoundException e) {

            logger.error("Error Outputting in @Controller all dynamic relevant recommendations for user_id: {}", user_id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with UUID " + user_id + " not found in database"));
        }
    }

}

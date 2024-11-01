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
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.AppError;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.service.UserRecommendationsService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/recommendation")
@Tag(
        name = "Контроллер рекомендаций",
        description = "Выполняет действия с рекомендациями")
public class UserRecommendationsController {

    private final Logger logger = LoggerFactory.getLogger(UserRecommendationsController.class);

    private final UserRecommendationsService userRecommendationsService;

    public UserRecommendationsController(UserRecommendationsService userRecommendationsService) {
        this.userRecommendationsService = userRecommendationsService;
    }

    @GetMapping(path = "/{user_id}")
    @Operation(
            summary = "Получение рекомендаций",
            description = "Позволяет получить рекомендации для пользователя")
    public ResponseEntity<Object> getUserRecommendation(
            @PathVariable
            @Parameter(description = "Идентификатор клиента") UUID user_id) {
        logger.info("Received request for getting all relevant recommendations for user_id: {}", user_id);

        try {
            UserRecommendationsDTO result = userRecommendationsService.getAllRecommendations(user_id);
            logger.info("Outputting in @Controller all relevant recommendations for user_id: {}", user_id);
            return ResponseEntity.ok(result);

        } catch (UserNotFoundException e) {

            logger.error("Error Outputting in @Controller all relevant recommendations for user_id: {}", user_id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with UUID " + user_id + " not found in database"));
        }
    }

}

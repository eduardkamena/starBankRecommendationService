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
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;
import pro.sky.recommendation_service.service.UserFixedRecommendationsService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/recommendation")
@Tag(
        name = "Контроллер рекомендаций продукта(ов) для клиента",
        description = "Выполняет действия с рекомендациями продукта(ов) для клиента")
public class UserTotalRecommendationsController {

    private final Logger logger = LoggerFactory.getLogger(UserTotalRecommendationsController.class);

    private final UserFixedRecommendationsService userFixedRecommendationsService;
    private final UserDynamicRecommendationsService userDynamicRecommendationsService;

    public UserTotalRecommendationsController(UserFixedRecommendationsService userFixedRecommendationsService, UserDynamicRecommendationsService userDynamicRecommendationsService) {
        this.userFixedRecommendationsService = userFixedRecommendationsService;
        this.userDynamicRecommendationsService = userDynamicRecommendationsService;
    }

    @GetMapping(path = "/fixed/{userId}")
    @Operation(
            summary = "Получение фиксированных рекомендаций продукта(ов) для клиента",
            description = "Позволяет получить фиксированные рекомендации продукта(ов) для клиента")
    public ResponseEntity<Object> getUserFixedRecommendations(
            @PathVariable
            @Parameter(description = "Идентификатор клиента") UUID userId) {
        logger.info("Received request for getting all fixed relevant recommendations for userId: {}", userId);

        try {
            UserRecommendationsDTO result = userFixedRecommendationsService.getAllFixedRecommendations(userId);
            logger.info("Outputting in @Controller all fixed relevant recommendations for userId: {}", userId);
            return ResponseEntity.ok(result);

        } catch (UserNotFoundException e) {

            logger.error("Error Outputting in @Controller all fixed relevant recommendations for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with UUID " + userId + " not found in database"));
        }
    }

    @GetMapping(path = "/dynamic/{userId}")
    @Operation(
            summary = "Получение динамических рекомендаций продукта(ов) для клиента",
            description = "Позволяет получить динамические рекомендации продукта(ов) для клиента")
    public ResponseEntity<Object> getUserDynamicRecommendations(
            @PathVariable
            @Parameter(description = "Идентификатор клиента") UUID userId) {
        logger.info("Received request for getting all dynamic relevant recommendations for userId: {}", userId);

        try {
            UserRecommendationsDTO result = userDynamicRecommendationsService.getAllDynamicRecommendations(userId);
            logger.info("Outputting in @Controller all dynamic relevant recommendations for userId: {}", userId);
            return ResponseEntity.ok(result);

        } catch (UserNotFoundException e) {

            logger.error("Error Outputting in @Controller all dynamic relevant recommendations for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with UUID " + userId + " not found in database"));
        }
    }

}

package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.service.RecommendationService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/recommendation")
@Tag(
        name = "Контроллер рекомендаций",
        description = "Выполняет действия с рекомендациями")
public class RecommendationsController {

    private final RecommendationService recommendationService;

    public RecommendationsController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping(path = "/{user_id}")
    @Operation(
            summary = "Получение рекомендаций",
            description = "Позволяет получить рекомендации для пользователя")
    public ResponseEntity<UserRecommendationsDTO> getUserRecommendation(
            @PathVariable @Parameter(description = "Идентификатор клиента") UUID user_id) {
        UserRecommendationsDTO result = recommendationService.getAllRecommendations(user_id);
        return ResponseEntity.ok(result);
    }

}

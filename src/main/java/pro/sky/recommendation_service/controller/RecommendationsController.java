package pro.sky.recommendation_service.controller;

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
public class RecommendationsController {

    private final RecommendationService recommendationService;

    public RecommendationsController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping(path = "/{user_id}")
    public ResponseEntity<UserRecommendationsDTO> getUserRecommendation(@PathVariable UUID user_id) {
        UserRecommendationsDTO result = recommendationService.getAllRecommendations(user_id);
        return ResponseEntity.ok(result);
    }

}

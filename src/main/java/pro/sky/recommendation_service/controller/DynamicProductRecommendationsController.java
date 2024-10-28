package pro.sky.recommendation_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation_service.dto.DynamicRecommendationDTO;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.service.DynamicProductRecommendationsService;
import pro.sky.recommendation_service.service.impl.DynamicProductRecommendationsImpl;

import java.util.List;

@RestController
@RequestMapping("/dynamic")
@RequiredArgsConstructor
public class DynamicProductRecommendationsController {

    private final DynamicProductRecommendationsImpl dynamicProductRecommendationsService;

    @PostMapping("/add")
    public String createRecommendation(@RequestBody DynamicRecommendationDTO recommendation) {
        dynamicProductRecommendationsService.createDynamicRecommendations(recommendation);
        return "done";
    }

    @GetMapping("/list")
    public List<DynamicProductRecommendation> getRecommendations() {
        return dynamicProductRecommendationsService.getAllDynamicRecommendations();
    }

}

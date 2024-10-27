package pro.sky.recommendation_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.dto.DynamicRecommendationDTO;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.service.DynamicProductRecommendationsService;

import java.util.List;

@RestController
@RequestMapping("/dynamic")
@RequiredArgsConstructor
public class DynamicProductRecommendationsController {

    private final DynamicProductRecommendationsService dynamicProductRecommendationsService;

    @PostMapping("/add")
//    public ResponseEntity<RecommendationDTO> createRecommendation(@RequestBody RecommendationDTO recommendation) {
//        RecommendationDTO createdRecommendation = dynamicProductRecommendationsService.createRecommendation(recommendation);
//        return new ResponseEntity<>(createdRecommendation, HttpStatus.OK);
//    }
    public String add(@RequestBody DynamicRecommendationDTO recommendation) {
        System.out.println(recommendation.toString());
        return "done";
    }

}

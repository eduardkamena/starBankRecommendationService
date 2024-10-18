package pro.sky.recommendation_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.service.RecommendationService;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class RecommendationsController {

    private final RecommendationService recommendationService;

    // Тестовый GET-метод. Для проверки можно использовать user.id - 958c44cd-f82c-4d7d-97c5-eb1dc45ce6b5
    // Результат - 8018
    @GetMapping(path = "test_JdbcTemplate_random_transaction_amount")
    public ResponseEntity<Integer> getRandomTransactionAmount(UUID user_id, String transactionType, String productsType) {
        String t = transactionType.toUpperCase();
        String p = productsType.toUpperCase();
        Integer randomTransactionAmount = recommendationService.getRandomTransactionAmount(user_id, t, p);
        return ResponseEntity.ok(randomTransactionAmount);
    }

}

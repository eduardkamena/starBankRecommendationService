package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
@Tag(
        name = "Контроллер кеша рекомендаций",
        description = "Выполняет действия с кешем рекомендаций")
public class CacheController {

    private final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @PostMapping("/clear-caches")
    @Operation(
            summary = "Сброс кэша рекомендаций",
            description = "Позволяет осуществить сброс кэша рекомендаций")
    @CacheEvict(value = "recommendationCache", allEntries = true)
    public ResponseEntity<Void> clearCaches() {

        logger.info("Successfully reset cache");

        return ResponseEntity.ok().build();
    }

}

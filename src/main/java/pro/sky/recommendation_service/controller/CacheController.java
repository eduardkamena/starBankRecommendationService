package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(path = "/management")
@Tag(
        name = "Контроллер кеша рекомендаций",
        description = "Выполняет действия с кешем рекомендаций")
public class CacheController {

    private final Logger logger = LoggerFactory.getLogger(CacheController.class);

    private final CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostMapping(path = "/clear-caches")
    @Operation(
            summary = "Сброс кэша рекомендаций",
            description = "Позволяет осуществить сброс кэша рекомендаций")
    @Caching(evict = {
            @CacheEvict(cacheNames = "dynamicRecommendations", allEntries = true),
            @CacheEvict(cacheNames = "fixedRecommendations", allEntries = true),
            @CacheEvict(cacheNames = "productRecommendations", allEntries = true),
            @CacheEvict(cacheNames = "telegramBot", allEntries = true)
    })
    public ResponseEntity<Void> clearCaches() {

        logger.info("Received request for cleaning caches");
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(
                        cacheManager.getCache(cacheName)).clear());
        logger.info("Cache was cleaned successfully");
        return ResponseEntity.ok().build();

    }

}

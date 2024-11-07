package pro.sky.recommendation_service.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class CacheController {
    @PostMapping("/clear-caches")
    @CacheEvict(value = "recommendationCache", allEntries = true)
    public ResponseEntity<Void> clearCaches() {
        return ResponseEntity.ok().build();
    }
}
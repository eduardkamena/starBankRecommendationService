package pro.sky.recommendation_service.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

/**
 * Конфигурация кеширования
 */
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    /**
     * Создание кэш-менеджера на основе {@link CaffeineCacheManager Caffeine}
     *
     * @return кеш-менеджер
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(1000));
        return cacheManager;
    }

    /**
     * Создание генератора ключей для кэшированных объектов
     *
     * @return генератор ключей на основе конфигурации {@link CacheKeyGeneratorConfig}
     */
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CacheKeyGeneratorConfig();
    }

}

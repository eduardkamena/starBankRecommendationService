package pro.sky.recommendation_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h2>Сервис банковских рекомендаций</h2>
 * <p>
 * Программа реализует сервис подбора рекомендаций продуктов
 * для пользователя, в соответствии с условиями по которым
 * происходит проверка/выборка.
 * <p>
 *     Программа получает запросы и передает ответы
 *     используя TelegramBot.
 *
 * <h2>Функциональность</h2>
 * <ul>
 *     <li>Получение пользователя</li>
 *     <li>Анализ условий и выборка подходящих продуктов</li>
 *     <li>Отправка рекомендуемых продуктов пользователю через TelegramBot</li>
 * </ul>
 *
 * @author IND25 Team_3
 * @version 0.1.0
 * @since 17.10.2024
 */

@SpringBootApplication
@OpenAPIDefinition
public class RecommendationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationServiceApplication.class, args);
    }

}

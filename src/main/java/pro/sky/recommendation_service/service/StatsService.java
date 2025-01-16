package pro.sky.recommendation_service.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Интерфейс для работы со статистикой срабатывания рекомендаций.
 * <p>
 * Определяет методы для увеличения счетчика срабатываний и получения статистики.
 */
public interface StatsService {

    /**
     * Увеличение счетчика срабатывания рекомендации.
     *
     * @param recommendationsId идентификатор рекомендации
     */
    void incrementStatsCount(UUID recommendationsId);

    /**
     * Получение статистики срабатывания рекомендаций.
     *
     * @return список статистики срабатывания рекомендаций
     */
    List<Map<String, ? extends Serializable>> getAllStatsCount();

}

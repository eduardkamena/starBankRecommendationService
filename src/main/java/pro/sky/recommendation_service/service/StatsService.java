package pro.sky.recommendation_service.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StatsService {

    void incrementStatsCount(UUID recommendationsId);

    List<Map<String, ? extends Serializable>> getAllStatsCount();

}

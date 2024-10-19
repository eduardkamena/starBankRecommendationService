package pro.sky.recommendation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserRecommendationsDTO {

    private final UUID user_id;
    private final List<RecommendationDTO> recommendations;

}

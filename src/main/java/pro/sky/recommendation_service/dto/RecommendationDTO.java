package pro.sky.recommendation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RecommendationDTO {

    private final String name;
    private final UUID id;
    private final String text;

}

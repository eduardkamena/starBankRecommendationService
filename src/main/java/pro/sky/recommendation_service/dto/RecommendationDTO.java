package pro.sky.recommendation_service.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class RecommendationDTO {

    String name;
    UUID id;
    String text;

}

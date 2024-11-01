package pro.sky.recommendation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendationsDTO {

    private String product_name;
    private UUID product_id;
    private String product_text;

}

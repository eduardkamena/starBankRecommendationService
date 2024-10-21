package pro.sky.recommendation_service.enitites;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductRecommendation {
    private String id;
    private String productName;
    private UUID productId;
    private List<Rule> rulesForProduct;
}

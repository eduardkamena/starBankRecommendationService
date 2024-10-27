package pro.sky.recommendation_service.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_recommendations")
public class DynamicProductRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recommendation_id")
    private UUID id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @OneToMany
    @JoinColumn(name = "id")
    private List<RecommendationRule> recommendationRules;
}

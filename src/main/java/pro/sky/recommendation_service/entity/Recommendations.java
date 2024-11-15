package pro.sky.recommendation_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "@Entity рекомендации")
@Table(name = "recommendations")
public class Recommendations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор правила рекомендации в БД (primary key)")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Schema(description = "Название рекомендации")
    @Column(name = "product_name", columnDefinition = "TEXT", nullable = false)
    private String productName;

    @Schema(description = "Идентификатор рекомендации в продакшн")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Schema(description = "Описание рекомендации")
    @Column(name = "product_text", columnDefinition = "TEXT", nullable = false)
    private String productText;

    @JsonIgnoreProperties(value = "recommendations", allowSetters = true)
    @OneToMany(mappedBy = "recommendations", cascade = CascadeType.ALL)
    @Schema(description = "Правила рекомендации")
    private List<Rules> rule;

    @JsonIgnoreProperties(value = "recommendations", allowSetters = true)
    @OneToOne(mappedBy = "recommendations", cascade = CascadeType.ALL)
    @Schema(description = "Счетчик срабатывания рекомендаций")
    private Stats stats;

}

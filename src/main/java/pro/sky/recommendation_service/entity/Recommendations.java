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
public class Recommendations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор правила рекомендации в БД (primary key)")
    private UUID id;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Название рекомендации")
    private String product_name;

    @Schema(description = "Идентификатор рекомендации в продакшн")
    private UUID product_id;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Описание рекомендации")
    private String product_text;

    @JsonIgnoreProperties(value = "recommendations", allowSetters = true)
    @OneToMany(mappedBy = "recommendations", cascade = CascadeType.ALL)
    @Schema(description = "Правила рекомендации")
    private List<Rules> rule;

    @JsonIgnoreProperties(value = "recommendations", allowSetters = true)
    @OneToOne(mappedBy = "recommendations", cascade = CascadeType.ALL)
    @Schema(description = "Счетчик срабатывания рекомендаций")
    private Stats stats;

}

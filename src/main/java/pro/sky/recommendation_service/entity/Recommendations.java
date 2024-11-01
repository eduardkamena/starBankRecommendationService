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
@Schema(description = "Сущность рекомендации")
public class Recommendations {

    @Id
    @Schema(description = "ID рекомендации")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Название рекомендации")
    private String product_name;

    @Schema(description = "Идентификатор рекомендации")
    private UUID product_id;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Описание рекомендации")
    private String product_text;

    @JsonIgnoreProperties(value = "recommendations", allowSetters = true)
    @OneToMany(mappedBy = "recommendations", cascade = CascadeType.ALL)
    private List<Rules> rule;

}

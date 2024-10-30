package pro.sky.recommendation_service.entity;


import jakarta.persistence.*;
import lombok.Data;
import pro.sky.recommendation_service.entity.enums.Queries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "rules")
public class RecommendationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "query")
    private Queries query;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "arguments" , joinColumns = @JoinColumn(name = "argument_id"))
    @Column(name = "argument" , nullable = false)
    private List<String> arguments = new ArrayList<>();

    @Column(name = "negate")
    private boolean negate;

}

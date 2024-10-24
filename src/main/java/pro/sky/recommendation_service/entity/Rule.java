package pro.sky.recommendation_service.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Rule {

    private UUID id;
    private String query;
    private String arguments;
    private boolean negate;

}

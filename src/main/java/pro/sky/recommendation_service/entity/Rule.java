package pro.sky.recommendation_service.entity;

import lombok.Data;

@Data
public class Rule {

    private String query;
    private String arguments;
    private boolean negate;

}

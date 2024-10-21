package pro.sky.recommendation_service.enitites;

import lombok.Data;

import java.util.List;

@Data
public class Rule {

    private String query;
    private List<String> arguments;
    private boolean negate;

}

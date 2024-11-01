package pro.sky.recommendation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RulesDTO {

    private UUID id;
    private String query;
    private List<String> arguments;
    private boolean negate;

}

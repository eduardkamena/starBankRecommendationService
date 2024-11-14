package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
@Tag(
        name = "Контроллер управления сервисом",
        description = "Выполняет действия с сервисом")
public class BuildInfoController {

    private final BuildProperties buildProperties;

    public BuildInfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping (path = "/info")
    @Operation(
            summary = "Возврат информации о сервисе",
            description = "Позволяет осуществить возврат информации о сервисе")
    public ResponseEntity<BuildProperties> getBuildInfo() {
        return ResponseEntity.ok(buildProperties);
    }

}

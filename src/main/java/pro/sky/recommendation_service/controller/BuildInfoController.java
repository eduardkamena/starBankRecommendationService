package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер информации о сервисе
 */
@RestController
@RequestMapping(path = "/management")
@Tag(
        name = "Контроллер управления сервисом",
        description = "Выполняет действия с сервисом")
public class BuildInfoController {

    private final Logger logger = LoggerFactory.getLogger(BuildInfoController.class);

    private final BuildProperties buildProperties;

    public BuildInfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    /**
     * Метод получения информации о сервисе
     *
     * @return информацию о сервисе полученную из build-info.properties
     */
    @GetMapping(path = "/info")
    @Operation(
            summary = "Возврат информации о сервисе",
            description = "Позволяет осуществить возврат информации о сервисе")
    public ResponseEntity<BuildProperties> getBuildInfo() {

        logger.info("Received request for projects build info");
        return ResponseEntity.ok(buildProperties);
    }

}

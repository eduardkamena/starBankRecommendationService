package pro.sky.recommendation_service.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Базовая конфигурация API
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Recommendation Service API",
                description = "Recommendation Service", version = "0.1.0"
        ),
        servers = @Server(
                url = "http://localhost:8085/",
                description = "Developer server"
        )
)
public class ApiConfig {

}

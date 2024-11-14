package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Сущность клиента")
public record UserDTO(@Schema(description = "Идентификатор клиента в БД") UUID id,
                      @Schema(description = "Псевдоним клиента в БД") String userName,
                      @Schema(description = "Имя клиента в БД") String first_name,
                      @Schema(description = "Фамилия клиента в БД") String last_name) {

}

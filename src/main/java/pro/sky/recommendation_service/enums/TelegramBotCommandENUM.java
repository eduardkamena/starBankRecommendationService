package pro.sky.recommendation_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление возможных команд для Telegram-бота.
 * <p>
 * Используется для обработки команд, отправленных пользователем в Telegram.
 */
@Getter
@AllArgsConstructor
public enum TelegramBotCommandENUM {

    RECOMMEND("/recommend"),
    START("/start"),
    HELP("/help");

    private final String command;

}

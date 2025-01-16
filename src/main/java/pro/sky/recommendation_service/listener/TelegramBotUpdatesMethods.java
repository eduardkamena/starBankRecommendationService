package pro.sky.recommendation_service.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.dto.UserDTO;
import pro.sky.recommendation_service.enums.TelegramBotEmojiENUM;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

/**
 * Класс, содержащий методы для обработки команд Telegram-бота.
 * <p>
 * Используется для формирования ответов на команды пользователей.
 */
@Component
@Data
@AllArgsConstructor
public class TelegramBotUpdatesMethods {

    private final UserDynamicRecommendationsService userDynamicRecommendationsService;

    private final String STICKER = "CAACAgIAAxkBAAEJ7LJnMQWrHjz2qx3Yls1ZJugsDHZ-WwACRwIAAkcVaAnk9we1CELDHjYE";

    /**
     * Формирует приветственное сообщение для пользователя.
     *
     * @param firstName имя пользователя
     * @return приветственное сообщение
     */
    String sendHelloMessage(String firstName) {
        return "Привет, " + firstName + "! "
                + TelegramBotEmojiENUM.RAISED_HANDS.get() + "\n"
                + TelegramBotEmojiENUM.ROBOT_FACE.get() +
                " Бот находит лучшие рекомендации продуктов" +
                " Банка \"Стар\" для своих клиентов.\n" +
                "Чтобы узнать как им пользоваться введи команду */help*";
    }

    /**
     * Формирует сообщение с инструкцией по использованию бота.
     *
     * @return сообщение с инструкцией
     */
    String sendHelpMessage() {
        return TelegramBotEmojiENUM.STAR.get() +
                " Чтобы узнать, какие продукты больше всего тебе подходят,\n" +
                "отправь команду */recommend* и свой *username* через пробел.\n" +
                "Например: */recommend luke.skywalker*";
    }

    /**
     * Формирует сообщение об ошибке, если список пользователей пуст.
     *
     * @return сообщение об ошибке
     */
    String sendNullMessage() {
        return TelegramBotEmojiENUM.PILL.get() +
                " Я сломался, мне нужен доктор...";
    }

    /**
     * Формирует сообщение об ошибке, если передано слишком много аргументов.
     *
     * @return сообщение об ошибке
     */
    String sendLengthMessage() {
        return TelegramBotEmojiENUM.X.get() +
                " Нужно написать после /recommend *только*" +
                " свой *username*, *ничего больше*!";
    }

    /**
     * Формирует сообщение об ошибке, если пользователь не найден.
     *
     * @return сообщение об ошибке
     */
    String sendUserListMessage() {
        return TelegramBotEmojiENUM.DISAPPOINTED.get() +
                " Пользователь не найден (";
    }

    /**
     * Формирует сообщение с рекомендациями для пользователя.
     *
     * @param userDTO данные пользователя
     * @return сообщение с рекомендациями
     */
    String sendSuccessMessage(UserDTO userDTO) {
        String recommendation = userDynamicRecommendationsService
                .getAllDynamicRulesRecommendationsForTelegramBot(userDTO.id());
        return (TelegramBotEmojiENUM.TADA.get() +
                " *Поиск нашел рекомендацию!*\n\n" +
                "Здравствуйте, *%s* *%s*!\n\n" +
                "Новые продукты для вас:\n\n\n%s").formatted(
                userDTO.firstName(),
                userDTO.lastName(),
                recommendation);
    }

    /**
     * Формирует сообщение об ошибке, если команда не распознана.
     *
     * @return сообщение об ошибке
     */
    String sendErrorMessage() {
        return "Нужно обязательно указать через *пробел* после" +
                " */recommend* свой *username*\n\n"
                + TelegramBotEmojiENUM.WRITING_HAND.get() +
                " Например:\n*/recommend jabba.hutt*";
    }

    /**
     * Формирует сообщение об ошибке, если команда не распознана.
     *
     * @return сообщение об ошибке
     */
    String sendElseMessage() {
        return TelegramBotEmojiENUM.X.get() +
                " *Сообщение не распознано.*\n" +
                "Повторите ввод.\n\n" +
                "Чтобы начать новый поиск, отправьте: /start";
    }

}

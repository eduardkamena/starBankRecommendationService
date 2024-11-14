package pro.sky.recommendation_service.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.dto.UserDTO;
import pro.sky.recommendation_service.enums.TelegramBotEmojiENUM;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

@Component
@Data
@AllArgsConstructor
public class TelegramBotUpdatesMethods {

    private final UserDynamicRecommendationsService userDynamicRecommendationsService;

    private final String STICKER = "CAACAgIAAxkBAAEJ7LJnMQWrHjz2qx3Yls1ZJugsDHZ-WwACRwIAAkcVaAnk9we1CELDHjYE";

    String sendHelloMessage(String firstName) {
        return "Привет, " + firstName + "! "
                + TelegramBotEmojiENUM.RAISED_HANDS.get() + "\n" +
                TelegramBotEmojiENUM.ROBOT_FACE.get() +
                " Бот находит лучшие рекомендации продуктов Банка \"Стар\" для своих клиентов.\n" +
                "Чтобы узнать как им пользоваться введи команду */help*";
    }

    String sendHelpMessage() {
        return TelegramBotEmojiENUM.STAR.get() +
                " Чтобы узнать, какие продукты больше всего тебе подходят,\n" +
                "отправь команду */recommend* и свой *username* через пробел.\n" +
                "Например: */recommend luke.skywalker*";
    }

    String sendNullMessage() {
        return TelegramBotEmojiENUM.PILL.get() +
                " Я сломался, мне нужен доктор...";
    }

    String sendLengthMessage() {
        return TelegramBotEmojiENUM.X.get() +
                " Нужно написать после /recommend *только* свой *username*, *ничего больше* !";
    }

    String sendUserListMessage() {
        return TelegramBotEmojiENUM.DISAPPOINTED.get() +
                " Пользователь не найден (";
    }

    String sendSuccessMessage(UserDTO userDTO) {
        String recommendation = userDynamicRecommendationsService
                .getAllDynamicRulesRecommendationsForTelegramBot(userDTO.id());
        return (TelegramBotEmojiENUM.TADA.get() + " *Поиск нашел рекомендацию!*\n\n" +
                "Здравствуйте, *%s* *%s*!\n\n" +
                "Новые продукты для вас:\n\n\n%s").formatted(
                userDTO.first_name(),
                userDTO.last_name(),
                recommendation);
    }

    String sendErrorMessage() {
        return "Нужно обязательно указать через *пробел* после */recommend* свой *username*\n\n"
                + TelegramBotEmojiENUM.WRITING_HAND.get() + " Например:\n*/recommend jabba.hutt*";
    }

    String sendElseMessage() {
        return TelegramBotEmojiENUM.X.get() +
                " *Сообщение не распознано.*\n" +
                "Повторите ввод.\n\n" +
                "Чтобы начать новый поиск, отправьте: /start";
    }

}

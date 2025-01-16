package pro.sky.recommendation_service.enums;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

/**
 * Перечисление возможных эмодзи для Telegram-бота.
 * <p>
 * Используется для отправки эмодзи в сообщениях Telegram.
 */
@AllArgsConstructor
public enum TelegramBotEmojiENUM {

    WRITING_HAND(":writing_hand:"),
    X(":x:"),
    ROBOT_FACE(":robot_face:"),
    TADA(":tada:"),
    RAISED_HANDS(":raised_hands:"),
    STAR(":star:"),
    PILL(":pill:"),
    DISAPPOINTED(":disappointed:");

    private final String value;

    /**
     * Возвращает эмодзи в виде строки Unicode.
     *
     * @return строка Unicode, представляющая эмодзи
     */
    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

}

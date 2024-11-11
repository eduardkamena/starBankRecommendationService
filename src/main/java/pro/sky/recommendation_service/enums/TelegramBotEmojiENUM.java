package pro.sky.recommendation_service.enums;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

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

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

}

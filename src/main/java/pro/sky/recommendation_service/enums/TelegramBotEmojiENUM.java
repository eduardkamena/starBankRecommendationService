package pro.sky.recommendation_service.enums;

import com.vdurmont.emoji.EmojiParser;

public enum TelegramBotEmojiENUM {

    WRITING_HAND(":writing_hand:"),
    MINUS(":heavy_minus_sign:"),
    CHECK(":white_check_mark:"),
    NOT(":x:"),
    DOUBT(":zzz:"),
    FLAG(":checkered_flag:");

    private final String value;

    TelegramBotEmojiENUM(String value) {
        this.value = value;
    }

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

}

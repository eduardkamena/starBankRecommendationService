package pro.sky.recommendation_service.service;

import com.pengrad.telegrambot.model.reaction.ReactionTypeEmoji;

public interface MessageSenderService {

    void sendMessage(Long chatID, String messageText);

    void sendSticker(Long chatID, String fileId);

    void sendReaction(Long chatID, int messageId, ReactionTypeEmoji reactionType);

}

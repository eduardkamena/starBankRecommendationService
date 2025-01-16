package pro.sky.recommendation_service.service;

import com.pengrad.telegrambot.model.reaction.ReactionTypeEmoji;

/**
 * Интерфейс для отправки сообщений, стикеров и реакций в Telegram.
 * <p>
 * Определяет методы для взаимодействия с Telegram-ботом.
 */
public interface MessageSenderService {

    /**
     * Отправка сообщения пользователю.
     *
     * @param chatID      идентификатор чата
     * @param messageText текст сообщения
     */
    void sendMessage(Long chatID, String messageText);

    /**
     * Отправка стикера пользователю.
     *
     * @param chatID идентификатор чата
     * @param fileId идентификатор стикера
     */
    void sendSticker(Long chatID, String fileId);

    /**
     * Отправка реакции на сообщение.
     *
     * @param chatID       идентификатор чата
     * @param messageId    идентификатор сообщения
     * @param reactionType тип реакции
     */
    void sendReaction(Long chatID, int messageId, ReactionTypeEmoji reactionType);

}

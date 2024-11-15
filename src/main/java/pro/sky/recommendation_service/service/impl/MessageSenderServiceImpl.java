package pro.sky.recommendation_service.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.reaction.ReactionTypeEmoji;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import com.pengrad.telegrambot.request.SetMessageReaction;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.service.MessageSenderService;

/**
 * <h3>Класс для отправки пользователю сообщения в TG
 *
 * <p>В данной реализации позволяет:
 * <ln>
 * <li>Отправлять сообщения пользователю см.{@link MessageSenderServiceImpl#sendMessage sendMessage}</li>
 * <li>Отправлять стикеры пользователю см.{@link MessageSenderServiceImpl#sendSticker sendSticker}</li>
 * <li>Отправлять реакции на сообщения пользователя см.{@link MessageSenderServiceImpl#sendReaction sendReaction}</li>
 * </ln>
 */
@Service
public class MessageSenderServiceImpl implements MessageSenderService {

    private final Logger logger = LoggerFactory.getLogger(MessageSenderServiceImpl.class);

    private final TelegramBot telegramBot;

    @Autowired
    public MessageSenderServiceImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Метод отправки сообщений от бота к пользователю
     *
     * @param chatID      идентификатор TG-чата
     * @param messageText текст передаваемого сообщения
     */
    @Override
    public void sendMessage(Long chatID, String messageText) {
        SendMessage sendMessage = new SendMessage(chatID, messageText).parseMode(ParseMode.Markdown);
        SendResponse sendResponse = telegramBot.execute(sendMessage);

        if (sendResponse.isOk()) {
            logger.info("Message for user {} with text \"{}\" was sent successfully", chatID, messageText);
        } else {
            logger.error("Unsuccessfully sending message for user {} with text \"{}\"", chatID, messageText);
        }
    }

    /**
     * Метод отправки Sticker от бота к пользователю
     *
     * @param chatID идентификатор TG-чата
     * @param fileId идентификатор файла эмодзи
     */
    @Override
    public void sendSticker(Long chatID, String fileId) {
        SendSticker sendSticker = new SendSticker(chatID, fileId);
        SendResponse sendResponse = telegramBot.execute(sendSticker);

        if (sendResponse.isOk()) {
            logger.info("Sticker for user {} with sticker id {} was sent successfully", chatID, fileId);
        } else {
            logger.error("Unsuccessfully sending sticker for user {} with sticker id {}", chatID, fileId);
        }
    }

    /**
     * Метод отправки реакции на сообщение от бота к пользователю
     *
     * @param chatID       идентификатор TG-чата
     * @param messageId    идентификатор сообщения внутри чата
     * @param reactionType тип эмодзи
     */
    @Override
    public void sendReaction(Long chatID, int messageId, ReactionTypeEmoji reactionType) {
        SetMessageReaction messageReaction = new SetMessageReaction(chatID, messageId, reactionType);
        BaseResponse sendResponse = telegramBot.execute(messageReaction);

        if (sendResponse.isOk()) {
            logger.info("Reaction for message id {} for user {} was sent successfully", messageId, chatID);
        } else {
            logger.error("Unsuccessfully sending reaction for message id {} for user {}", messageId, chatID);
        }
    }

}

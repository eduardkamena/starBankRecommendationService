package pro.sky.recommendation_service.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.service.MessageSenderService;

@Service
public class MessageSenderServiceImpl implements MessageSenderService {

    private final Logger logger = LoggerFactory.getLogger(MessageSenderServiceImpl.class);

    @Autowired
    private TelegramBot telegramBot;

    // Метод отправки сообщений от бота к пользователю
    @Override
    public void sendMessage(Long chatID, String messageText) {
        SendMessage sendMessage = new SendMessage(chatID, messageText);
        SendResponse sendResponse = telegramBot.execute(sendMessage);

        if (sendResponse.isOk()) {
            logger.info("Message for user {} with text \"{}\" was sent successfully", chatID, messageText);
        } else {
            logger.error("Unsuccessfully sending message for user {} with text \"{}\"", chatID, messageText);
        }
    }

}

package pro.sky.recommendation_service.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.reaction.ReactionTypeEmoji;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.UserDTO;
import pro.sky.recommendation_service.enums.TelegramBotCommandENUM;
import pro.sky.recommendation_service.repository.TelegramBotRepository;
import pro.sky.recommendation_service.service.MessageSenderService;

import java.util.Collection;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TelegramBotRepository telegramBotRepository;
    private final MessageSenderService messageSenderService;
    private final TelegramBotUpdatesMethods telegramBotUpdatesMethods;

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      TelegramBotRepository telegramBotRepository,
                                      MessageSenderService messageSenderService,
                                      TelegramBotUpdatesMethods telegramBotUpdatesMethods) {
        this.telegramBot = telegramBot;
        this.telegramBotRepository = telegramBotRepository;
        this.messageSenderService = messageSenderService;
        this.telegramBotUpdatesMethods = telegramBotUpdatesMethods;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Message message = update.message();

            if (message == null) {
                logger.error("Received unsupported message type {}", update);
                return;
            }

            Long chatId = message.chat().id();
            String messageText = message.text();
            String[] splitMessageText = messageText.split(" ");

            if (TelegramBotCommandENUM.START
                    .getCommand()
                    .equals(messageText)) {
                String firstName = message.from().firstName();

                logger.info("Successfully send START message for user {}", chatId);
                messageSenderService.sendMessage(
                        chatId,
                        telegramBotUpdatesMethods.sendHelloMessage(firstName));

            } else if (TelegramBotCommandENUM.HELP
                    .getCommand()
                    .equals(messageText)) {

                logger.info("Successfully send HELP message for user {}", chatId);
                messageSenderService.sendMessage(
                        chatId,
                        telegramBotUpdatesMethods.sendHelpMessage());

            } else if (TelegramBotCommandENUM.RECOMMEND
                    .getCommand()
                    .equals(splitMessageText[0])) {

                try {
                    // Проставление ботом реакции на сообщение
                    messageSenderService.sendReaction(
                            chatId,
                            message.messageId(),
                            new ReactionTypeEmoji("✍️"));

                    Collection<UserDTO> userList = telegramBotRepository.getUser(
                            splitMessageText[1].toLowerCase());

                    if (userList == null) {
                        logger.warn("User list is null");
                        messageSenderService.sendMessage(
                                chatId,
                                telegramBotUpdatesMethods.sendNullMessage());

                    } else if (splitMessageText.length > 2) {
                        logger.warn("Need only 2 arguments, now: {}", splitMessageText.length);
                        messageSenderService.sendMessage(
                                chatId,
                                telegramBotUpdatesMethods.sendLengthMessage());

                    } else if (userList.size() != 1) {
                        logger.warn("Received users amount {}", userList.size());
                        messageSenderService.sendMessage(
                                chatId,
                                telegramBotUpdatesMethods.sendUserListMessage());

                    } else {
                        userList.forEach(userDTO -> {
                            logger.info("Recommend products to username {} was send successfully", userList);

                            // Отправка Sticker перед результатом
                            messageSenderService.sendSticker(
                                    chatId,
                                    telegramBotUpdatesMethods.getSTICKER());

                            messageSenderService.sendMessage(
                                    chatId,
                                    telegramBotUpdatesMethods.sendSuccessMessage(userDTO));
                        });
                    }

                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    logger.error("Received empty username and catch an exception", e);
                    messageSenderService.sendMessage(
                            chatId,
                            telegramBotUpdatesMethods.sendErrorMessage());
                }

            } else {
                logger.info("Else message to username {} was send successfully", chatId);
                messageSenderService.sendReaction(
                        chatId,
                        message.messageId(),
                        new ReactionTypeEmoji("🤷‍♂️"));
                messageSenderService.sendMessage(
                        chatId,
                        telegramBotUpdatesMethods.sendElseMessage());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}

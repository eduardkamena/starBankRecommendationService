package pro.sky.recommendation_service.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.UserDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.enums.TelegramBotEmojiENUM;
import pro.sky.recommendation_service.repository.TelegramBotRepository;
import pro.sky.recommendation_service.service.MessageSenderService;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

import java.util.Collection;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TelegramBotRepository telegramBotRepository;
    private final UserDynamicRecommendationsService userDynamicRecommendationsService;
    private final MessageSenderService messageSenderService;

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramBotRepository telegramBotRepository, UserDynamicRecommendationsService userDynamicRecommendationsService, MessageSenderService messageSenderService) {
        this.telegramBot = telegramBot;
        this.telegramBotRepository = telegramBotRepository;
        this.userDynamicRecommendationsService = userDynamicRecommendationsService;
        this.messageSenderService = messageSenderService;
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

            if ("/recommend".equals(splitMessageText[0])) {

                try {
                    Collection<UserDTO> userList = telegramBotRepository.getUser(splitMessageText[1].toLowerCase());

                    if (userList == null) {
                        logger.warn("User list is null");
                        String messageError = ("Я сломался, мне нужен доктор!");
                        messageSenderService.sendMessage(chatId, messageError);

                    } else if (userList.size() != 1) {
                        logger.warn("Received users amount {}", userList.size());
                        String messageError = ("Пользователь не найден!");
                        messageSenderService.sendMessage(chatId, messageError);

                    } else {
                        userList.forEach(userDto -> {
                            UserRecommendationsDTO recommendation = userDynamicRecommendationsService.getAllDynamicRecommendations(userDto.id());
                            String messageUser = ("Здравствуйте, %s %s!\n" +
                                    "Новые продукты для вас: %s").formatted(
                                    userDto.first_name(),
                                    userDto.last_name(),
                                    recommendation.recommendations());
                            logger.info("Message to username {} was saved successfully", userList);
                            messageSenderService.sendMessage(chatId, messageUser);
                        });
                    }

                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    logger.error("Received empty username and catch an exception", e);
                    String messageError = ("Нужно обязательно указать свой username" + TelegramBotEmojiENUM.WRITING_HAND.get() + "\nнапример: \n/recommend jabba.hutt");
                    messageSenderService.sendMessage(chatId, messageError);
                }
            }

            switch (messageText) {
                case "/help":
                    messageSenderService.sendMessage(chatId, getHelp());
                    break;
                case "/start":
                    String nameUser = message.from().firstName();
                    messageSenderService.sendMessage(chatId, getHello(nameUser));
                    break;
                default:
                    messageSenderService.sendMessage(chatId, "Я еще не понимаю, что такое " + messageText);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private String getHelp() {
        return "Чтобы узнать какие продукты больше всего тебе подходят\n" +
                "пропиши команду /recommend и свое имя.\n"
                + "К примеру /recommend luke.skywalker";
    }

    private String getHello(String firstName) {
        logger.info("Hello bot {}", firstName);
        return "Привет " + firstName + "!\n"
                + "Этот телеграм бот предназначен для нахождения самых лучших продуктов для пользоватлей.\n"
                + "Чтобы узнать как им пользоваться введи команду /help.";
    }

}

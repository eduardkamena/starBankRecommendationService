package pro.sky.recommendation_service.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.dto.UserDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.repository.TelegramBotRepository;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

import java.util.Collection;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotRepository telegramBotRepository;

    @Autowired
    private UserDynamicRecommendationsService userDynamicRecommendationsService;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String message = update.message().text();
            String[] splitMessage = message.split(" ");
            if (splitMessage[0].equals("/recommend")) {
                Collection<UserDTO> userList = telegramBotRepository.getUser(splitMessage[1]);
                userList.forEach(userDto -> {
                    UserRecommendationsDTO recommendation = userDynamicRecommendationsService.getAllDynamicRecommendations(userDto.id());
                    String messageUser = ("Здравствуйте %s %s\n" +
                            "Новые продукты для вас: %s").formatted(
                            userDto.first_name(),
                            userDto.last_name(),
                            recommendation.recommendations());
                    SendMessage sendMessage = new SendMessage(update.message().chat().id(),
                            messageUser);
                    telegramBot.execute(sendMessage);
                });
            }

            SendMessage sendMessage;
            switch (update.message().text()) {
                case "/help":
                    sendMessage = new SendMessage(update.message().chat().id(),
                            getHelp());
                    telegramBot.execute(sendMessage);
                    break;
                case "/start":
                    String nameUser = update.message().from().firstName();
                    sendMessage = new SendMessage(update.message().chat().id(),
                            getHello(nameUser));
                    telegramBot.execute(sendMessage);
                    break;

            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private String getHelp() {
        return "Чтобы узнать какие продукты больше всего тебе подходят\n" +
                "пропиши команду /recommend и свое имя.\n"
                + "К примеру /recommend sheron.berge";
    }

    private String getHello(String firstName) {
        logger.info("Hello bot {}", firstName);
        return "Привет " + firstName + "!\n"
                + "Этот телеграм бот предназначен для нахождения самых лучших продуктов для пользоватлей.\n"
                + "Чтобы узнать как им пользоваться введи команду /help.";
    }

}

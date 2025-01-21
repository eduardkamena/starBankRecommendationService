package pro.sky.recommendation_service.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pro.sky.recommendation_service.enums.TelegramBotEmojiENUM;
import pro.sky.recommendation_service.repository.TelegramBotRepository;
import pro.sky.recommendation_service.service.MessageSenderService;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class TelegramBotUpdatesListenerUnitTest {

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private TelegramBotRepository telegramBotRepository;

    @MockBean
    private MessageSenderService messageSenderService;

    @MockBean
    private TelegramBotUpdatesMethods telegramBotUpdatesMethods;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    void shouldProcessUpdates() {
        // given
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        User user = Mockito.mock(User.class);
        Chat chat = Mockito.mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.from()).thenReturn(user);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(user.firstName()).thenReturn("Test");

        // Ожидаемое сообщение
        String expectedMessage = "Привет, Test! "
                + TelegramBotEmojiENUM.RAISED_HANDS.get() + "\n"
                + TelegramBotEmojiENUM.ROBOT_FACE.get()
                + " Бот находит лучшие рекомендации продуктов"
                + " Банка \"Стар\" для своих клиентов.\n"
                + "Чтобы узнать как им пользоваться введи команду */help*";

        // when
        when(telegramBotUpdatesMethods.sendHelloMessage(eq("Test")))
                .thenReturn(expectedMessage);
        telegramBotUpdatesListener.process(List.of(update));

        // then
        verify(messageSenderService, times(1)).sendMessage(eq(123L), eq(expectedMessage));
    }

}

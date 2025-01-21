package pro.sky.recommendation_service.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageSenderServiceImplUnitTest {

    @MockBean
    private TelegramBot telegramBot;

    @Autowired
    private MessageSenderServiceImpl messageSenderService;

    @Test
    void shouldSendMessage() {
        // given
        Long chatId = 12345L;
        String messageText = "Test Message";
        SendResponse sendResponse = Mockito.mock(SendResponse.class);

        when(telegramBot.execute(any(SendMessage.class))).thenReturn(sendResponse);
        when(sendResponse.isOk()).thenReturn(true);

        // when
        messageSenderService.sendMessage(chatId, messageText);

        // then
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

}

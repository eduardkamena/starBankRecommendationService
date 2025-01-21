package pro.sky.recommendation_service.listener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.recommendation_service.dto.UserDTO;
import pro.sky.recommendation_service.service.UserDynamicRecommendationsService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
class TelegramBotUpdatesMethodsUnitTest {

    private final UserDynamicRecommendationsService userDynamicRecommendationsService = mock(UserDynamicRecommendationsService.class);

    private final TelegramBotUpdatesMethods telegramBotUpdatesMethods = new TelegramBotUpdatesMethods(userDynamicRecommendationsService);

    @Test
    void shouldSendHelloMessage() {
        // given
        String firstName = "Test";

        // when
        String message = telegramBotUpdatesMethods.sendHelloMessage(firstName);

        // then
        assertTrue(message.contains("Привет, " + firstName));
    }

    @Test
    void shouldSendSuccessMessage() {
        // given
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "testUser", "Test", "User");

        // when
        String message = telegramBotUpdatesMethods.sendSuccessMessage(userDTO);

        // then
        assertTrue(message.contains("Здравствуйте, *Test* *User*!"));
    }

}

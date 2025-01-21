package pro.sky.recommendation_service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import pro.sky.recommendation_service.configuration.JDBCTestConfig;
import pro.sky.recommendation_service.dto.UserDTO;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TelegramBotRepository.class, JDBCTestConfig.class}) // Импортируем репозиторий и конфигурацию для тестирования
@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TelegramBotRepositoryH2Test {

    @Autowired
    private TelegramBotRepository telegramBotRepository;

    @Test
    void shouldReturnUserWhenUsernameExists() {
        // given
        String username = "testUser"; // Пользователь, который есть в data.sql

        // when
        Collection<UserDTO> result = telegramBotRepository.getUser(username);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        UserDTO user = result.iterator().next();
        assertEquals(UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d"), user.id());
        assertEquals("testUser", user.userName());
        assertEquals("Test", user.firstName());
        assertEquals("User", user.lastName());
    }

    @Test
    void shouldReturnEmptyCollectionWhenUsernameDoesNotExist() {
        // given
        String username = "unknownUser"; // Пользователь, которого нет в data.sql

        // when
        Collection<UserDTO> result = telegramBotRepository.getUser(username);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}

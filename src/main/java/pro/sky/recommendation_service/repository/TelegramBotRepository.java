package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.dto.UserDTO;

import java.util.Collection;

/**
 * Репозиторий для работы с данными пользователей через JDBC.
 * <p>
 * Используется для выполнения SQL-запросов к базе данных.
 */
@Repository
public class TelegramBotRepository {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TelegramBotRepository(
            @Qualifier("recommendationsJdbcTemplate")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение данных пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return коллекция данных пользователя
     */
    @Cacheable(cacheNames = "telegramBot", key = "#root.methodName + #username")
    public Collection<UserDTO> getUser(String username) {
        String sql = "SELECT u.ID, u.USERNAME, u.FIRST_NAME, u.LAST_NAME " +
                "          FROM USERS u " +
                "          WHERE u.USERNAME = ?";

        Collection<UserDTO> result = jdbcTemplate.query(sql, new DataClassRowMapper<>(UserDTO.class), username);

        logger.info("Executing a SQL query \"isUserExists\" in the database for userName: {}", username);
        return result;
    }

}

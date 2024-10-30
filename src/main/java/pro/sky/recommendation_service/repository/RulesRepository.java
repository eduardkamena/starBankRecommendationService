package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.dto.RulesDTO;

import java.sql.Types;
import java.util.Arrays;

@Repository
public class RulesRepository {

    private final Logger logger = LoggerFactory.getLogger(RulesRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RulesRepository(
            @Qualifier("postgresDataSource")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // SQL-запрос на запись объекта запроса в БД
    public RulesDTO createRule(RulesDTO rulesDTO) {
        String sanitizedQuery = rulesDTO.getQuery().toUpperCase();
        String[] sanitizedArguments = Arrays.stream(rulesDTO.getArguments())
                .map(String::toUpperCase)
                .toArray(String[]::new);

        String sql = "INSERT INTO rules (query, arguments, negate) " +
                "VALUES (?, array[?], ?)";

        logger.info("Starting SQL-query for creating rule in database for rule: {}", rulesDTO);
        jdbcTemplate.update(sql,
                sanitizedQuery,
                new SqlParameterValue(Types.ARRAY, sanitizedArguments),
                rulesDTO.isNegate());

        logger.info("Successfully creating rule in database for rule: {}", rulesDTO);
        return new RulesDTO(rulesDTO.getQuery(), rulesDTO.getArguments(), rulesDTO.isNegate());
    }

    // SQL-запрос на запись объекта запроса в БД
    public void deleteRule(String query) {
        String sanitizedQuery = query.toUpperCase().replaceAll("\"", "");
        String sql = "DELETE FROM rules r WHERE r.query = ?";

        logger.info("Starting SQL-query for deleting rule in database for rule.query: {}", sanitizedQuery);
        int result = jdbcTemplate.update(sql, sanitizedQuery);

        if (result == 0) {
            logger.warn("No rule found with query: {}", sanitizedQuery);
        } else {
            logger.info("Successfully deleted rule in database for rule.query: {}", sanitizedQuery);
        }
    }

}

package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.dto.RulesDTO;

import java.sql.Types;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

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
        String sql = "INSERT INTO rules (query, arguments, negate) " +
                "VALUES (?, array[?], ?)";

        logger.info("Starting SQL-query for creating rule in database for rule: {}", rulesDTO);

        jdbcTemplate.update(sql, rulesDTO.getQuery(), new SqlParameterValue(Types.ARRAY, rulesDTO.getArguments()), rulesDTO.isNegate());

        logger.info("Successfully creating rule in database for rule: {}", rulesDTO);

        return new RulesDTO(rulesDTO.getQuery(), rulesDTO.getArguments(), rulesDTO.isNegate());

    }

}

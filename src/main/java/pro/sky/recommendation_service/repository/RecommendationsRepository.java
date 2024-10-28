package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.dto.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final Logger logger = LoggerFactory.getLogger(RecommendationsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RecommendationsRepository(
            @Qualifier("postgresDataSource")
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Запрос в БД на наличие/отсутствия пользователя
    public Optional<RecommendationDTO> isProductExists(UUID productId) {
        String sql = "SELECT product_name, product_id , product_text " +
                "FROM RECOMMENDATIONS r " +
                "WHERE r.product_id = ?";
        try {
            RecommendationDTO recommendationDTO = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                String name = rs.getString("product_name");
                UUID id = UUID.fromString(rs.getString("product_id"));
                String description = rs.getString("product_text");
                return new RecommendationDTO(name, id, description);
            }, productId);
            logger.info("Product found in the database for productId: {}", productId);
            return Optional.ofNullable(recommendationDTO);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Product not found in the database for productId: {}", productId);
            return Optional.empty();
        }
    }
}

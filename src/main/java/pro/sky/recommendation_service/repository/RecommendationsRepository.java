package pro.sky.recommendation_service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    // Запрос в БД на наличие/отсутствия продукта
    public Optional<RecommendationDTO> getProductDescription(UUID product_id) {
        String sql = "SELECT product_name, product_id , product_text " +
                "FROM RECOMMENDATIONS r " +
                "WHERE r.product_id = ?";

        logger.info("Starting SQL-query for product in database for product_id: {}", product_id);

        RecommendationDTO recommendationDTO = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> {
                    String name = rs.getString("product_name");
                    UUID id = UUID.fromString(rs.getString("product_id"));
                    String description = rs.getString("product_text");
                    return new RecommendationDTO(name, id, description);
                },
                product_id);

        logger.info("Product found in the database for product_id: {}", product_id);
        assert recommendationDTO != null;
        return Optional.of(recommendationDTO);

    }

}

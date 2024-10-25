package pro.sky.recommendation_service.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation_service.entity.Product;
import pro.sky.recommendation_service.entity.Rule;

import java.util.UUID;

@Repository
public class ProductsRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductsRepository(
            @Qualifier("dynamicsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addProduct(Product product) {
        String addSql = "INSERT INTO products (id, name, description)  VALUES (?, ?, ?)";
        UUID uuid = UUID.randomUUID();

        jdbcTemplate.update(addSql, uuid, product.getName(), product.getDescription());
    }


    public void deleteProductById(UUID productId) {
        String deleteSql = "DELETE FROM products WHERE id = ?";

        jdbcTemplate.update(deleteSql, productId);
    }

}

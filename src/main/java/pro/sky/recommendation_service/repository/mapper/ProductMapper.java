package pro.sky.recommendation_service.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import pro.sky.recommendation_service.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();

        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));

        return product;
    }
}

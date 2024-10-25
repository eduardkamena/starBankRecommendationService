package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.entity.Product;
import pro.sky.recommendation_service.service.ProductsService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер продуктов",
        description = "Выполняет действия с продуктами для рекомендаций"
)
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping("/add")
    @Operation(
            summary = "Добавление продукта для рекомендаций",
            description = "Позволяет добавить продукт для рекомендаций на основе тела запроса"
    )
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productsService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }
}

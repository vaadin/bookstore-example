package org.vaadin.example.bookstore.backend.mock;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;
import org.vaadin.example.bookstore.backend.repositories.CategoryRepository;
import org.vaadin.example.bookstore.backend.repositories.ProductRepository;

/**
 * Seeds the in-memory database with mock products and categories on first
 * startup so the demo has data to display.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }
        List<Category> categories = categoryRepository
                .saveAll(MockDataGenerator.createCategories());
        List<Product> products = MockDataGenerator.createProducts(categories);
        productRepository.saveAll(products);
    }
}

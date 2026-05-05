package org.vaadin.example.bookstore.backend.services;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;
import org.vaadin.example.bookstore.backend.repositories.CategoryRepository;
import org.vaadin.example.bookstore.backend.repositories.ProductRepository;

@Service
@Transactional
public class DataServiceImpl implements DataService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataServiceImpl(ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Page<Product> list(Pageable pageable, Specification<Product> filter) {
        return productRepository.findAll(filter, pageable);
    }

    @Override
    public void updateProduct(Product p) {
        productRepository.save(p);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return;
        }
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (product.getCategory().removeIf(
                    c -> c.getId().equals(categoryId))) {
                productRepository.save(product);
            }
        }
        categoryRepository.delete(category);
    }
}

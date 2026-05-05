package org.vaadin.example.bookstore.backend.services;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public interface DataService extends Serializable {

    Collection<Product> getAllProducts();

    Collection<Category> getAllCategories();

    Page<Product> list(Pageable pageable, Specification<Product> filter);

    void updateProduct(Product p);

    void deleteProduct(Long productId);

    Product getProductById(Long productId);

    void updateCategory(Category category);

    void deleteCategory(Long categoryId);

}

package org.vaadin.example.bookstore.backend;

import java.io.Serializable;
import java.util.Collection;

import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;
import org.vaadin.example.bookstore.backend.mock.MockDataService;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class DataService implements Serializable {

    public abstract Collection<Product> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);

    public abstract void deleteProduct(int productId);

    public abstract Product getProductById(int productId);

    public abstract void updateCategory(Category category);

    public abstract void deleteCategory(int categoryId);

    public static DataService get() {
        return MockDataService.getInstance();
    }

}

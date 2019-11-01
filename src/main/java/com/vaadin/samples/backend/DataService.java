package com.vaadin.samples.backend;

import java.io.Serializable;
import java.util.Collection;

import com.vaadin.samples.backend.data.Category;
import com.vaadin.samples.backend.data.Product;
import com.vaadin.samples.backend.mock.MockDataService;

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

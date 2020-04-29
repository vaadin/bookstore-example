package org.vaadin.example.bookstore.backend.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.vaadin.example.bookstore.backend.DataService;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class MockDataService extends DataService {

    private static Map<Locale, MockDataService> instances = new HashMap<>();

    private List<Product> products;
    private List<Category> categories;
    private int nextProductId = 0;
    private int nextCategoryId = 0;

    private MockDataService(Locale locale) {
        MockDataGenerator.generateData(locale);
        categories = MockDataGenerator.createCategories();
        products = MockDataGenerator.createProducts(categories);
        nextProductId = products.size() + 1;
        nextCategoryId = categories.size() + 1;
    }

    public static synchronized DataService getInstance(Locale locale) {
        MockDataService localeData = instances.get(locale);
        if (localeData == null) {
            instances.put(locale, new MockDataService(locale));
        }
        return instances.get(locale);
    }

    @Override
    public synchronized List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public synchronized void updateProduct(Product p) {
        if (p.getId() < 0) {
            // New product
            p.setId(nextProductId++);
            products.add(p);
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == p.getId()) {
                products.set(i, p);
                return;
            }
        }

        throw new IllegalArgumentException("No product with id " + p.getId()
                + " found");
    }

    @Override
    public synchronized Product getProductById(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return products.get(i);
            }
        }
        return null;
    }

    @Override
    public void updateCategory(Category category) {
        if (category.getId() < 0) {
            category.setId(nextCategoryId++);
            categories.add(category);
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        if (categories.removeIf(category -> category.getId() == categoryId)) {
            getAllProducts().forEach(product -> {
                product.getCategory().removeIf(category -> category.getId() == categoryId);
            });
        }
    }

    @Override
    public synchronized void deleteProduct(int productId) {
        Product p = getProductById(productId);
        if (p == null) {
            throw new IllegalArgumentException("Product with id " + productId
                    + " not found");
        }
        products.remove(p);
    }
}

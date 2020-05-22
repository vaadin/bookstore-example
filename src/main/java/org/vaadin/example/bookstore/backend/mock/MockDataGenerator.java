package org.vaadin.example.bookstore.backend.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import org.vaadin.example.bookstore.backend.data.Availability;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;

public class MockDataGenerator {
    private static int nextCategoryId = 1;
    private static int nextProductId = 1;
    private static final Random random = new Random(1);

    private static String[] categoryNames;

    private static String[] word1;

    private static String[] word2;

    static void generateData(Locale locale) {
        ResourceBundle mockDataResource = ResourceBundle.getBundle("org.vaadin.example.bookstore.backend.resourcebundle.MockDataResource", locale);
        categoryNames = mockDataResource.getStringArray("categoryNames");
        word1 = mockDataResource.getStringArray("word1");
        word2 = mockDataResource.getStringArray("word2");
    }

    static List<Category> createCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (String name : categoryNames) {
            Category c = createCategory(name);
            categories.add(c);
        }
        return categories;

    }

    static List<Product> createProducts(List<Category> categories) {
        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < 100; i++) {
            Product p = createProduct(categories);
            products.add(p);
        }

        return products;
    }

    private static Category createCategory(String name) {
        Category c = new Category();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

    private static Product createProduct(List<Category> categories) {
        Product p = new Product();
        p.setId(nextProductId++);
        p.setProductName(generateName());

        p.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));

        Availability availability = Availability.values()[random.nextInt(Availability
                .values().length)];

        p.setAvailability(availability);
        p.setAvailabilityClass(availability.getName());
        if (p.getAvailability() == Availability.AVAILABLE) {
            p.setStockCount(random.nextInt(523));
        }

        p.setCategory(getCategory(categories, 1, 2));
        return p;
    }

    private static Set<Category> getCategory(List<Category> categories,
            int min, int max) {
        int nr = random.nextInt(max) + min;
        HashSet<Category> productCategories = new HashSet<Category>();
        for (int i = 0; i < nr; i++) {
            productCategories.add(categories.get(random.nextInt(categories
                    .size())));
        }

        return productCategories;
    }

    private static String generateName() {
        return word1[random.nextInt(word1.length)] + " "
                + word2[random.nextInt(word2.length)];
    }

}

package com.vaadin.samples.crud;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.samples.backend.data.Availability;
import com.vaadin.samples.backend.data.Category;
import com.vaadin.samples.backend.data.Product;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Grid of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ProductGrid extends Grid<Product> {

    public ProductGrid() {
        setSizeFull();

        addColumn(Product::getId).setHeader("Id").setFlexGrow(1);
        addColumn(Product::getProductName).setHeader("Product name").setFlexGrow(20);

        // Format and add " €" to price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        // To change the text alignment of the column, a template is used.
        final String priceTemplate = "<div style='text-align: right'>[[item.price]]</div>";
        addColumn(TemplateRenderer.<Product>of(priceTemplate)
                .withProperty("price", product -> decimalFormat.format(product.getPrice()) + " €"))
                .setHeader("Price")
                .setComparator(Comparator.comparing(Product::getPrice))
                .setFlexGrow(3);

        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css and are
        // used here in availabilityTemplate.
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availability]]\"></iron-icon> [[item.availability]]";
        addColumn(TemplateRenderer.<Product>of(availabilityTemplate)
                .withProperty("availability", product -> product.getAvailability().toString()))
                .setHeader("Availability")
                .setComparator(Comparator.comparing(Product::getAvailability))
                .setFlexGrow(5);

        // To change the text alignment of the column, a template is used.
        final String stockCountTemplate = "<div style='text-align: right'>[[item.stockCount]]</div>";
        addColumn(TemplateRenderer.<Product>of(stockCountTemplate)
                .withProperty("stockCount", product -> product.getStockCount() == 0 ? "-" : Integer.toString(product.getStockCount())))
                .setHeader("Stock count")
                .setComparator(Comparator.comparingInt(Product::getStockCount))
                .setFlexGrow(3);

        // Show all categories the product is in, separated by commas
        addColumn(this::formatCategories)
                .setHeader("Category")
                .setSortable(false)
                .setFlexGrow(12);
    }

    public Product getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Product product) {
        getDataCommunicator().refresh(product);
    }

    private String formatCategories(Product product) {
        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            return "";
        }
        return product.getCategory().stream()
                .sorted(Comparator.comparing(Category::getId))
                .map(Category::getName).collect(Collectors.joining(", "));
    }
}

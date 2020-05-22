package org.vaadin.example.bookstore.ui.inventory;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;

/**
 * Grid of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ProductGrid extends Grid<Product> {

    public ProductGrid() {

        setSizeFull();

        addColumn(Product::getProductName).setHeader("Product name")
                .setFlexGrow(20).setSortable(true).setKey("productname");

        // Format and add " €" to price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        addColumn(product -> decimalFormat.format(product.getPrice()) + " €")
                .setHeader("Price").setTextAlign(ColumnTextAlign.END)
                .setComparator(Comparator.comparing(Product::getPrice))
                .setFlexGrow(3).setKey("price");

        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css
        // and are
        // used here in availabilityTemplate.
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availability]]\"></iron-icon> [[item.availability]]";
        addColumn(TemplateRenderer.<Product>of(availabilityTemplate)
                .withProperty("availability",
                        product -> product.getAvailability().toString()))
                                .setHeader("Availability")
                                .setComparator(Comparator
                                        .comparing(Product::getAvailability))
                                .setFlexGrow(5).setKey("availability");

        addColumn(product -> product.getStockCount() == 0 ? "-"
                : Integer.toString(product.getStockCount()))
                        .setHeader("Stock count")
                        .setTextAlign(ColumnTextAlign.END)
                        .setComparator(
                                Comparator.comparingInt(Product::getStockCount))
                        .setFlexGrow(3).setKey("stock");

        // Show all categories the product is in, separated by commas
        addColumn(this::formatCategories).setHeader("Category").setFlexGrow(12)
                .setKey("category");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(true);
            getColumnByKey("stock").setVisible(true);
            getColumnByKey("category").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(false);
            getColumnByKey("stock").setVisible(false);
            getColumnByKey("category").setVisible(true);
        } else {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(false);
            getColumnByKey("stock").setVisible(false);
            getColumnByKey("category").setVisible(false);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> {
            setColumnVisibility(e.getBodyClientWidth());
        });
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

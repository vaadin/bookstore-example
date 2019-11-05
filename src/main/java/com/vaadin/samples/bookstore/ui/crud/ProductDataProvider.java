package com.vaadin.samples.bookstore.ui.crud;

import java.util.Locale;
import java.util.Objects;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.samples.bookstore.backend.DataService;
import com.vaadin.samples.bookstore.backend.data.Product;

public class ProductDataProvider extends ListDataProvider<Product> {

    /** Text filter that can be changed separately. */
    private String filterText = "";

    public ProductDataProvider() {
        super(DataService.get().getAllProducts());
    }

    /**
     * Store given product to the backing data service.
     * 
     * @param product
     *            the updated or new product
     */
    public void save(Product product) {
        boolean newProduct = product.isNewProduct();

        DataService.get().updateProduct(product);
        if (newProduct) {
            refreshAll();
        } else {
            refreshItem(product);
        }
    }

    /**
     * Delete given product from the backing data service.
     * 
     * @param product
     *            the product to be deleted
     */
    public void delete(Product product) {
        DataService.get().deleteProduct(product.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for product name, availability and category.
     * 
     * @param filterText
     *            the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();

        setFilter(product -> passesFilter(product.getProductName(), filterText)
                || passesFilter(product.getAvailability(), filterText)
                || passesFilter(product.getCategory(), filterText));
    }

    @Override
    public Integer getId(Product product) {
        Objects.requireNonNull(product,
                "Cannot provide an id for a null product.");

        return product.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}

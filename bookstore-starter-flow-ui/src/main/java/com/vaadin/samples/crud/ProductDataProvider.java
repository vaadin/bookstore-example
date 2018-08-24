package com.vaadin.samples.crud;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.samples.backend.DataService;
import com.vaadin.samples.backend.data.Product;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public class ProductDataProvider
        extends AbstractDataProvider<Product, String> {
    
    /** Text filter that can be changed separately. */
    private String filterText = "";

    /**
     * Store given product to the backing data service.
     * 
     * @param product
     *            the updated or new product
     */
    public void save(Product product) {
        boolean newProduct = product.getId() == -1;
        
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
     * Sets the filter to use for the this data provider and refreshes data.
     * <p>
     * Filter is compared for product name, availability and category.
     * 
     * @param filterText
     *           the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();
        
        refreshAll();
    }
    
    @Override
    public Integer getId(Product product) {
        Objects.requireNonNull(product, "Cannot provide an id for a null product.");
        
        return product.getId();
    }
    
    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public int size(Query<Product, String> t) {
        return (int) fetch(t).count();
    }

    @Override
    public Stream<Product> fetch(Query<Product, String> query) {
        if (filterText.isEmpty()) {
            return DataService.get().getAllProducts().stream()
                    .skip(query.getOffset()).limit(query.getLimit());
        }
        return DataService.get().getAllProducts().stream().filter(
                product -> passesFilter(product.getProductName(), filterText)
                        || passesFilter(product.getAvailability(), filterText)
                        || passesFilter(product.getCategory(), filterText))
                .skip(query.getOffset()).limit(query.getLimit());
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}

package org.vaadin.example.bookstore.ui.inventory;

import java.util.Locale;
import java.util.Objects;

import com.vaadin.flow.data.provider.ListDataProvider;
import org.vaadin.example.bookstore.backend.DataService;
import org.vaadin.example.bookstore.backend.data.Product;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Product} entities.
 * <p>
 * Used to simplify the code in {@link SampleCrudView} and
 * {@link SampleCrudLogic}.
 */
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
        final boolean newProduct = product.isNewProduct();

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
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(product -> passesFilter(product.getProductName(), this.filterText)
                || passesFilter(product.getAvailability(), this.filterText)
                || passesFilter(product.getCategory(), this.filterText));
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

package org.vaadin.example.bookstore.ui.inventory;

import java.util.Locale;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.example.bookstore.backend.data.Product;
import org.vaadin.example.bookstore.backend.services.DataService;
import org.vaadin.example.bookstore.ui.MainLayout;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link InventoryViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Inventory", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class InventoryView extends HorizontalLayout
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Inventory";
    private final ProductGrid grid;
    private final ProductForm form;
    private TextField filter;

    private final InventoryViewLogic viewLogic;
    private Button newProduct;

    private final DataService dataService;

    public InventoryView(DataService dataService) {
        this.dataService = dataService;
        viewLogic = new InventoryViewLogic(this, dataService);
        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new ProductGrid();
        // Configure Grid with filtering support
        grid.setItems(query -> {
            Specification<Product> spec = Specification.unrestricted();
            String filterText = filter.getValue();
            if (filterText != null && !filterText.isEmpty()) {
                spec = spec.and((root, criteriaQuery,
                                 criteriaBuilder) -> criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("productName")),
                        "%" + filterText.toLowerCase(Locale.ENGLISH) + "%"));
            }
            return dataService.list(
                    PageRequest.of(query.getPage(), query.getPageSize(),
                            VaadinSpringDataHelpers.toSpringDataSort(query)),
                    spec).stream();
        });
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        form = new ProductForm(viewLogic);
        form.setCategories(dataService.getAllCategories());
        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setId("grid-filter");
        filter.setPlaceholder("Filter name, availability or category");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> grid.getDataProvider().refreshAll());
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newProduct = new Button("New product");
        // Setting theme variant of new production button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newProduct.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newProduct.addClickListener(click -> viewLogic.newProduct());
        // A shortcut to click the new product button by pressing ALT + N
        newProduct.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newProduct);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    /**
     * Shows a temporary popup notification to the user.
     * 
     * @see Notification#show(String)
     * @param msg
     */
    public void showNotification(String msg) {
        Notification.show(msg);
    }

    /**
     * Enables/Disables the new product button.
     * 
     * @param enabled
     */
    public void setNewProductEnabled(boolean enabled) {
        newProduct.setEnabled(enabled);
    }

    /**
     * Deselects the selected row in the grid.
     */
    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    /**
     * Selects a row
     * 
     * @param row
     */
    public void selectRow(Product row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a product in the list of products.
     * 
     * @param product
     */
    public void updateProduct(Product product) {
        final boolean newProduct = product.isNewProduct();

        dataService.updateProduct(product);
        if (newProduct) {
            grid.getDataProvider().refreshAll();
        } else {
            grid.getDataProvider().refreshItem(product);
        }
    }

    /**
     * Removes a product from the list of products.
     * 
     * @param product
     */
    public void removeProduct(Product product) {
        dataService.deleteProduct(product.getId());
        grid.getDataProvider().refreshAll();
    }

    /**
     * Displays user a form to edit a Product.
     * 
     * @param product
     */
    public void editProduct(Product product) {
        showForm(product != null);
        form.editProduct(product);
    }

    /**
     * Shows and hides the new product form
     * 
     * @param show
     */
    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
            @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}

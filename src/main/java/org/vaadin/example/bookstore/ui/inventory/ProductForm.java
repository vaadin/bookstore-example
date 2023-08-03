package org.vaadin.example.bookstore.ui.inventory;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.example.bookstore.backend.data.Availability;
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.data.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * A form for editing a single product.
 */
public class ProductForm extends Div {

    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    private final VerticalLayout content;

    private final TextField productName;
    private final BigDecimalField price;
    private final IntegerField stockCount;
    private final Select<Availability> availability;
    private final CheckboxGroup<Category> category;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final InventoryViewLogic viewLogic;
    private final Binder<Product> binder;
    private Product currentProduct;

    private static class PriceConverter implements Converter<BigDecimal, BigDecimal> {
        @Override
        public Result<BigDecimal> convertToModel(BigDecimal value, ValueContext context) {
            return Result.ok(value.setScale(2, RoundingMode.HALF_DOWN));
        }

        @Override
        public BigDecimal convertToPresentation(BigDecimal value, ValueContext context) {
            return value.setScale(2, RoundingMode.HALF_DOWN);
        }
    }

    public ProductForm(InventoryViewLogic sampleCrudLogic) {
        setClassName("product-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("product-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        productName = new TextField(resourceBundle.getString("product_name"));
        productName.setWidth("100%");
        productName.setRequired(true);
        productName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(productName);

        price = new BigDecimalField(resourceBundle.getString("price"));
        price.setSuffixComponent(new Span("€"));
        price.setValueChangeMode(ValueChangeMode.EAGER);

        stockCount = new IntegerField(resourceBundle.getString("in_stock"));
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);

        final HorizontalLayout horizontalLayout = new HorizontalLayout(price,
                stockCount);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, price, stockCount);
        content.add(horizontalLayout);

        availability = new Select<>();
        availability.setLabel(resourceBundle.getString("availability"));
        availability.setWidth("100%");
        availability.setItems(Availability.values());
        content.add(availability);

        category = new CheckboxGroup<>();
        category.setLabel(resourceBundle.getString("categories"));
        category.setId("category");
        category.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        content.add(category);

        binder = new BeanValidationBinder<>(Product.class);
        binder.forField(price).withConverter(new PriceConverter()).bind("price");
        binder.forField(stockCount).bind("stockCount");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button(resourceBundle.getString("save"));
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProduct(currentProduct);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button(resourceBundle.getString("discard"));
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editProduct(currentProduct));

        cancel = new Button(resourceBundle.getString("cancel"));
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelProduct());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");

        delete = new Button(resourceBundle.getString("delete"));
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProduct(currentProduct);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void setCategories(Collection<Category> categories) {
        category.setItems(categories);
    }

    public void editProduct(Product product) {
        if (product == null) {
            product = new Product();
        }
        delete.setVisible(!product.isNewProduct());
        currentProduct = product;
        binder.readBean(product);
    }
}

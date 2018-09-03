package com.vaadin.samples.crud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.samples.backend.data.Availability;
import com.vaadin.samples.backend.data.Category;
import com.vaadin.samples.backend.data.Product;
import org.vaadin.pekka.CheckboxGroup;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * A form for editing a single product.
 */
public class ProductForm extends VerticalLayout {
    private TextField productName;
    private TextField price;
    private TextField stockCount;
    private ComboBox<Availability> availability;
    private CheckboxGroup<Category> category;
    private Button save;
    private Button discard;
    private Button cancel;
    private Button delete;

    private SampleCrudLogic viewLogic;
    private Binder<Product> binder;
    private Product currentProduct;

    private static class StockPriceConverter extends StringToIntegerConverter {

        public StockPriceConverter() {
            super("Could not convert value to " + Integer.class.getName());
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // do not use a thousands separator, as HTML5 input type
            // number expects a fixed wire/DOM number format regardless
            // of how the browser presents it to the user (which could
            // depend on the browser locale)
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }

        @Override
        public Result<Integer> convertToModel(String value,
                ValueContext context) {
            Result<Integer> result = super.convertToModel(value, context);
            return result.map(stock -> stock == null ? 0 : stock);
        }

    }

    public ProductForm(SampleCrudLogic sampleCrudLogic) {
        super();

        viewLogic = sampleCrudLogic;

        setHeight("100%");
        setWidth("397px");

        productName = new TextField("Product name");
        productName.setWidth("100%");
        productName.setRequired(true);
        productName.setValueChangeMode(ValueChangeMode.EAGER);
        add(productName);

        price = new TextField("Price");
        price.setWidth("44%");
        price.setValueChangeMode(ValueChangeMode.EAGER);

        stockCount = new TextField("In Stock");
        stockCount.setWidth("43%");
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout horizontalLayout = new HorizontalLayout(price,
                stockCount);
        add(horizontalLayout);

        availability = new ComboBox<>("Availability");
        availability.setWidth("100%");
        availability.setRequired(true);
        availability.setItems(Availability.values());
        availability.setAllowCustomValue(false);
        add(availability);

        category = new CheckboxGroup<>();
        category.setId("category");
        category.getContent().getStyle().set("flex-direction", "column");
        Label categoryLabel = new Label("Categories");
        categoryLabel.setClassName("vaadin-label");
        categoryLabel.setFor(category);
        add(categoryLabel, category);

        Div expander = new Div();
        add(expander);
        setFlexGrow(1, expander);

        binder = new BeanValidationBinder<>(Product.class);
        binder.forField(price).withConverter(new EuroConverter()).bind("price");
        binder.forField(stockCount).withConverter(new StockPriceConverter())
                .bind("stockCount");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.getElement().getThemeList().add("primary");
        save.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProduct(currentProduct);
            }
        });

        discard = new Button("Discard Changes");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> viewLogic.editProduct(currentProduct));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelProduct());
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.getElement().getThemeList()
                .addAll(Arrays.asList("error", "primary"));
        delete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProduct(currentProduct);
            }
        });

        add(save, discard, delete, cancel);
    }

    public void setCategories(Collection<Category> categories) {
        category.setItems(categories);
    }

    public void editProduct(Product product) {
        if (product == null) {
            product = new Product();
        }
        currentProduct = product;
        binder.readBean(product);
    }
}

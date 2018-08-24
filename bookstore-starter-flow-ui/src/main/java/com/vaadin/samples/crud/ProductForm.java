package com.vaadin.samples.crud;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.samples.backend.data.Availability;
import com.vaadin.samples.backend.data.Category;
import com.vaadin.samples.backend.data.Product;
import org.vaadin.pekka.CheckboxGroup;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

/**
 * A form for editing a single product.
 *
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
@HtmlImport("frontend://com/vaadin/samples/ProductFormDesign.html")
@Tag("product-form")
public class ProductForm extends PolymerTemplate<TemplateModel> {
    @Id("productName")
    protected TextField productName;

    @Id("price")
    private TextField price;

    @Id("stockCount")
    private TextField stockCount;

    @Id("availability")
    private ComboBox<Availability> availability;

    @Id("categoryContainer")
    private Div categoryContainer;

    // Since this CheckboxGroup does not have an equivalent web
    // component there is no @Id annotation here.
    private CheckboxGroup<Category> category;

    @Id("save")
    private Button save;

    @Id("discard")
    private Button discard;

    @Id("cancel")
    private Button cancel;

    @Id("delete")
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

        availability.setItems(Availability.values());
        availability.setAllowCustomValue(false);

        binder = new BeanValidationBinder<>(Product.class);
        binder.forField(price).withConverter(new EuroConverter())
                .bind("price");
        binder.forField(stockCount).withConverter(new StockPriceConverter())
                .bind("stockCount");

        category = new CheckboxGroup<>();
        category.getContent().getStyle().set("flex-direction", "column");
        categoryContainer.add(category);

        binder.forField(category).bind("category");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProduct(currentProduct);
            }
        });

        discard.addClickListener(
                event -> viewLogic.editProduct(currentProduct));

        cancel.addClickListener(event -> viewLogic.cancelProduct());

        delete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProduct(currentProduct);
            }
        });
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

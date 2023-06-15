package org.vaadin.example.bookstore.ui;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.vaadin.example.bookstore.backend.DataService;
import org.vaadin.example.bookstore.backend.data.Category;

/**
 * Admin view that is registered dynamically on admin user login.
 * <p>
 * Allows CRUD operations for the book categories.
 */
public class AdminView extends VerticalLayout {

    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    private final VirtualList<Category> categoriesListing;
    private final ListDataProvider<Category> dataProvider;
    private final Button newCategoryButton;

    public AdminView() {
        categoriesListing = new VirtualList<>();

        dataProvider = new ListDataProvider<Category>(
                new ArrayList<>(DataService.get(UI.getCurrent().getLocale()).getAllCategories()));
        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(
                new ComponentRenderer<>(this::createCategoryEditor));

        newCategoryButton = new Button(resourceBundle.getString("new_category"), event -> {
            final Category category = new Category();
            dataProvider.getItems().add(category);
            dataProvider.refreshAll();
        });
        newCategoryButton.setDisableOnClick(true);

        add(new H2(resourceBundle.getString("greeting") + " Admin"), new H4(resourceBundle.getString("edit_categories")), newCategoryButton,
                categoriesListing);
    }

    private Component createCategoryEditor(Category category) {
        final TextField nameField = new TextField();
        if (category.getId() < 0) {
            nameField.focus();
        }

        final Button deleteButton = new Button(
                VaadinIcon.MINUS_CIRCLE_O.create(), event -> {

                    // Ask for confirmation before deleting stuff
                    final ConfirmDialog dialog = new ConfirmDialog(
                            resourceBundle.getString("confirm_header"),
                            resourceBundle.getString("confirm_msg"),
                            resourceBundle.getString("delete"), () -> {
                                DataService.get(UI.getCurrent().getLocale())
                                        .deleteCategory(category.getId());
                                dataProvider.getItems().remove(category);
                                dataProvider.refreshAll();
                                Notification.show(resourceBundle.getString("category_deleted"));
                            });

                    dialog.open();

                });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        final BeanValidationBinder<Category> binder = new BeanValidationBinder<>(
                Category.class);
        binder.forField(nameField).bind("name");
        binder.setBean(category);
        binder.addValueChangeListener(event -> {
            if (binder.isValid()) {
                DataService.get(UI.getCurrent().getLocale()).updateCategory(category);
                deleteButton.setEnabled(true);
                newCategoryButton.setEnabled(true);
                Notification.show(resourceBundle.getString("category_saved"));
            }
        });
        deleteButton.setEnabled(category.getId() > 0);

        final HorizontalLayout layout = new HorizontalLayout(nameField,
                deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}

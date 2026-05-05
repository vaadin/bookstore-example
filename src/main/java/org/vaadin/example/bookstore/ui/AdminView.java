package org.vaadin.example.bookstore.ui;

import java.util.ArrayList;

import com.vaadin.flow.component.Component;
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
import org.vaadin.example.bookstore.backend.data.Category;
import org.vaadin.example.bookstore.backend.services.DataService;

/**
 * Admin view that is registered dynamically on admin user login.
 * <p>
 * Allows CRUD operations for the book categories.
 */
public class AdminView extends VerticalLayout {

    public static final String VIEW_NAME = "Admin";

    private final VirtualList<Category> categoriesListing;
    private final ListDataProvider<Category> dataProvider;
    private final Button newCategoryButton;
    private final DataService dataService;

    public AdminView(DataService dataService) {
        this.dataService = dataService;
        categoriesListing = new VirtualList<>();

        dataProvider = new ListDataProvider<Category>(
                new ArrayList<>(dataService.getAllCategories()));
        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(
                new ComponentRenderer<>(this::createCategoryEditor));

        newCategoryButton = new Button("Add New Category", event -> {
            final Category category = new Category();
            dataProvider.getItems().add(category);
            dataProvider.refreshAll();
        });
        newCategoryButton.setDisableOnClick(true);

        add(new H2("Hello Admin"), new H4("Edit Categories"), newCategoryButton,
                categoriesListing);
    }

    private Component createCategoryEditor(Category category) {
        final TextField nameField = new TextField();
        if (category.getId() == null) {
            nameField.focus();
        }

        final Button deleteButton = new Button(
                VaadinIcon.MINUS_CIRCLE_O.create(), event -> {

                    // Ask for confirmation before deleting stuff
                    final ConfirmDialog dialog = new ConfirmDialog(
                            "Please confirm",
                            "Are you sure you want to delete the category? Books in this category will not be deleted.",
                            "Delete", () -> {
                                dataService.deleteCategory(category.getId());
                                dataProvider.getItems().remove(category);
                                dataProvider.refreshAll();
                                Notification.show("Category Deleted.");
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
                dataService.updateCategory(category);
                deleteButton.setEnabled(true);
                newCategoryButton.setEnabled(true);
                Notification.show("Category Saved.");
            }
        });
        deleteButton.setEnabled(category.getId() != null);

        final HorizontalLayout layout = new HorizontalLayout(nameField,
                deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}

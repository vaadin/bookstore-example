package org.vaadin.example.bookstore.ui;

import java.util.ArrayList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import org.vaadin.example.bookstore.backend.DataService;
import org.vaadin.example.bookstore.backend.data.Category;

/**
 * Admin view that is registered dynamically on admin user login.
 * <p>
 * Allows CRUD operations for the book categories.
 */
public class AdminView extends VerticalLayout {

    public static final String VIEW_NAME = "Admin";

    private final IronList<Category> categoriesListing;
    private final ListDataProvider<Category> dataProvider;
    private final Button newCategoryButton;

    public AdminView() {
        categoriesListing = new IronList<>();

        dataProvider = new ListDataProvider<>(
                new ArrayList<>(DataService.get().getAllCategories()));
        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(
                new ComponentRenderer<>(this::createCategoryEditor));

        Button openCategoryDialog = new Button("Open category dialog",
                click -> {
                    Dialog dialog = new Dialog();
                    dialog.setHeaderTitle("Add a new category");
                    TextField categoryName = new TextField("Category name");
                    categoryName.setValueChangeMode(ValueChangeMode.EAGER);
                    Button button = new Button("Add category", e -> {
                        final Category category = new Category();
                        category.setName(categoryName.getValue());
                        DataService.get().updateCategory(category);
                        dataProvider.refreshAll();
                        dialog.close();
                    });
                    button.addClickShortcut(Key.KEY_N, KeyModifier.CONTROL);
                    dialog.add(categoryName, button);
                    dialog.open();
                });

        newCategoryButton = new Button("Add New Category", event -> {
            final Category category = new Category();
            dataProvider.getItems().add(category);
            dataProvider.refreshAll();
        });
        newCategoryButton.setDisableOnClick(true);
        newCategoryButton.addClickShortcut(Key.KEY_N, KeyModifier.CONTROL)
                .bindLifecycleTo(categoriesListing);

        categoriesListing.setVisible(false);

        Checkbox showCategories = new Checkbox("Show categories");
        showCategories.addValueChangeListener(click -> {
           categoriesListing.setVisible(click.getValue());
        });

        add(new H2("Hello Admin"), new H4("Edit Categories"),
                newCategoryButton, openCategoryDialog, showCategories,
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
                            "Please confirm",
                            "Are you sure you want to delete the category? Books in this category will not be deleted.",
                            "Delete", () -> {
                                DataService.get()
                                        .deleteCategory(category.getId());
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
                DataService.get().updateCategory(category);
                deleteButton.setEnabled(true);
                newCategoryButton.setEnabled(true);
                Notification.show("Category Saved.");
            }
        });
        deleteButton.setEnabled(category.getId() > 0);

        final HorizontalLayout layout = new HorizontalLayout(nameField,
                deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}

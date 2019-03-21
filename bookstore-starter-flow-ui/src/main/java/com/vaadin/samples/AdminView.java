package com.vaadin.samples;

import java.util.ArrayList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.samples.backend.DataService;
import com.vaadin.samples.backend.data.Category;

/**
 * Admin view that is registered dynamically on admin user login.
 * <p>
 * Allows CRUD operations for the book categories.
 */
public class AdminView extends VerticalLayout {

    public static final String VIEW_NAME = "admin";

    private final IronList<Category> categoriesListing;
    private final ListDataProvider<Category> dataProvider;
    private final Button newCategoryButton;

    public AdminView() {
        categoriesListing = new IronList<>();

        dataProvider = new ListDataProvider<Category>(new ArrayList<>(DataService.get().getAllCategories())) {
            @Override
            public Object getId(Category item) {
                return item.getId();
            }
        };
        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(new ComponentRenderer<>(this::createCategoryEditor));

        newCategoryButton = new Button("Add New Category", event -> {
            Category category = new Category();
            dataProvider.getItems().add(category);
            dataProvider.refreshAll();
        });
        newCategoryButton.setDisableOnClick(true);

        add(new H2("Hello Admin"), new H4("Edit Categories"), newCategoryButton, categoriesListing);
    }

    private Component createCategoryEditor(Category category) {
        TextField nameField = new TextField();
        if (category.getId() < 0) {
            nameField.focus();
        }

        Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), event -> {
            DataService.get().deleteCategory(category.getId());
            dataProvider.getItems().remove(category);
            dataProvider.refreshAll();
            Notification.show("Category Deleted.");
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        BeanValidationBinder<Category> binder = new BeanValidationBinder<>(Category.class);
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

        HorizontalLayout layout = new HorizontalLayout(nameField, deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

}

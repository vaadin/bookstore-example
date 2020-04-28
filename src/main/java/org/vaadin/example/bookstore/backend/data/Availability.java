package org.vaadin.example.bookstore.backend.data;

import com.vaadin.flow.component.UI;

import java.util.ResourceBundle;

public enum Availability {
    COMING("coming"), AVAILABLE("available"), DISCONTINUED("discontinued");

    private final String name;

    private Availability(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale()).getString(name);
    }
}

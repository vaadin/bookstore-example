package com.vaadin.samples;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.samples.about.AboutView;

/**
 * The layout of the pages e.g. About and Inventory.
 */
public class MainScreen extends HorizontalLayout implements RouterLayout {
    private Menu menu;

    public MainScreen() {

        setSpacing(false);
        setSizeFull();

        menu = new Menu();
        menu.addView(AboutView.class, AboutView.VIEW_NAME,
                VaadinIcon.INFO_CIRCLE.create());

        add(menu);
    }
}

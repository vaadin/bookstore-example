package org.vaadin.example.bookstore.ui.about;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;
import org.vaadin.example.bookstore.ui.MainLayout;

@Route(value = "About", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout {

    public static final String VIEW_NAME = "About";

    public AboutView() {
        add(VaadinIcon.INFO_CIRCLE.create());
        add(new Span(" This application is using Vaadin version "
                + Version.getFullVersion() + "."));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

    }
}

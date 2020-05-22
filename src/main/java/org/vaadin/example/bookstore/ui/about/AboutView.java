package org.vaadin.example.bookstore.ui.about;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;
import org.vaadin.example.bookstore.ui.MainLayout;

import java.util.ResourceBundle;

@Route(value = "About", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout {

    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    public AboutView() {
        add(VaadinIcon.INFO_CIRCLE.create());
        add(new Span(
                resourceBundle.getString("application_sign_prefix")
                + Version.getFullVersion() + ' ' + resourceBundle.getString("application_sign_suffix")));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}

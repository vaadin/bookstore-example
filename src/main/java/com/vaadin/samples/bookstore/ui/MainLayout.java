package com.vaadin.samples.bookstore.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.RouteBaseData;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.samples.bookstore.authentication.AccessControlFactory;
import com.vaadin.samples.bookstore.ui.about.AboutView;
import com.vaadin.samples.bookstore.ui.crud.SampleCrudView;

/**
 * The main layout. Contains the navigation menu.
 */
@Theme(value = Lumo.class)
@PWA(name = "Bookstore Starter", shortName = "Bookstore")
@CssImport("./styles/shared-styles.css")
public class MainLayout extends FlexLayout implements RouterLayout {
    private Menu menu;

    public MainLayout() {
        setSizeFull();
        setClassName("main-layout");

        menu = new Menu();
        menu.addView(SampleCrudView.class, SampleCrudView.VIEW_NAME,
                VaadinIcon.EDIT.create());
        menu.addView(AboutView.class, AboutView.VIEW_NAME,
                VaadinIcon.INFO_CIRCLE.create());

        add(menu);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        attachEvent.getUI()
                .addShortcutListener(
                        () -> AccessControlFactory.getInstance()
                                .createAccessControl().signOut(),
                        Key.KEY_L, KeyModifier.CONTROL);

        // add the admin view menu item if/when it is registered dynamically
        Command addAdminMenuItemCommand = () -> menu.addView(AdminView.class,
                AdminView.VIEW_NAME, VaadinIcon.DOCTOR.create());
        RouteConfiguration sessionScopedConfiguration = RouteConfiguration
                .forSessionScope();
        if (sessionScopedConfiguration.isRouteRegistered(AdminView.class)) {
            addAdminMenuItemCommand.execute();
        } else {
            sessionScopedConfiguration.addRoutesChangeListener(event -> {
                for (RouteBaseData data : event.getAddedRoutes()) {
                    if (data.getNavigationTarget().equals(AdminView.class)) {
                        addAdminMenuItemCommand.execute();
                    }
                }
            });
        }
    }
}

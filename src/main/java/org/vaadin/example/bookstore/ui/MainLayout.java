package org.vaadin.example.bookstore.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.example.bookstore.authentication.AccessControl;
import org.vaadin.example.bookstore.authentication.AccessControlFactory;
import org.vaadin.example.bookstore.ui.about.AboutView;
import org.vaadin.example.bookstore.ui.inventory.InventoryView;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The main layout. Contains the navigation menu.
 */
@Theme(value = Lumo.class)
@PWA(name = "Bookstore", shortName = "Bookstore")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainLayout extends AppLayout implements RouterLayout, LocaleChangeObserver {

    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    private final Button logoutButton;

    private Select<String> languageSelect;
    private static final String persian = "فارسی";
    private static final String english = "English";

    public MainLayout() {

        // Header of the menu (the navbar)

        // menu toggle
        final DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassName("menu-toggle");
        addToNavbar(drawerToggle);

        // image, logo
        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.setClassName("menu-header");

        // Note! Image resource url is resolved here as it is dependent on the
        // execution mode (development or production) and browser ES level
        // support
        final String resolvedImage = VaadinService.getCurrent().resolveResource(
                "img/table-logo.png");

        final Image image = new Image(resolvedImage, "");
        final Label title = new Label(resourceBundle.getString("bookstore"));
        top.add(image, title);
        top.add(title);

        // Add language selector
        languageSelect = new Select<>();
        languageSelect.setItems(english, persian);
        languageSelect.getElement().setAttribute("theme", "small");

        languageSelect.setValue("en".equals(UI.getCurrent().getLocale().getLanguage()) ? english : persian);

        languageSelect.addValueChangeListener(
                event -> {
                    if (english.equals(event.getValue())) {
                        VaadinSession.getCurrent().setLocale(Locale.ENGLISH);
                        UI.getCurrent().getPage().reload();
                    } else {
                        VaadinSession.getCurrent().setLocale(new Locale("fa", "IR"));
                        UI.getCurrent().getPage().reload();
                    }
                });

        languageSelect.setValue("en".equals(UI.getCurrent().getLocale().getLanguage()) ? english : persian);
        top.add(languageSelect);

        addToNavbar(top);

        // Navigation items
        addToDrawer(createMenuLink(InventoryView.class, resourceBundle.getString("inventory"),
                VaadinIcon.EDIT.create()));

        addToDrawer(createMenuLink(AboutView.class, resourceBundle.getString("about"),
                VaadinIcon.INFO_CIRCLE.create()));

        // Create logout button but don't add it yet; admin view might be added
        // in between (see #onAttach())
        logoutButton = createMenuButton(resourceBundle.getString("logout"), VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(e -> logout());
        logoutButton.getElement().setAttribute("title", resourceBundle.getString("logout") + " (Ctrl+L)");

    }

    private void logout() {
        AccessControlFactory.getInstance().createAccessControl().signOut();
    }

    private RouterLink createMenuLink(Class<? extends Component> viewClass,
            String caption, Icon icon) {
        final RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setClassName("menu-link");
        routerLink.add(icon);
        routerLink.add(new Span(caption));
        icon.setSize("24px");
        return routerLink;
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");
        return routerButton;
    }

    private void registerAdminViewIfApplicable(AccessControl accessControl) {
        // register the admin view dynamically only for any admin user logged in
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)
                && !RouteConfiguration.forSessionScope()
                        .isRouteRegistered(AdminView.class)) {
            RouteConfiguration.forSessionScope().setRoute("admin",
                    AdminView.class, MainLayout.class);
            // as logout will purge the session route registry, no need to
            // unregister the view on logout
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // User can quickly activate logout with Ctrl+L
        attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L,
                KeyModifier.CONTROL);

        // add the admin view menu item if user has admin role
        final AccessControl accessControl = AccessControlFactory.getInstance()
                .createAccessControl();
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {

            // Create extra navigation target for admins
            registerAdminViewIfApplicable(accessControl);

            // The link can only be created now, because the RouterLink checks
            // that the target is valid.
            addToDrawer(createMenuLink(AdminView.class, resourceBundle.getString("admin"),
                    VaadinIcon.DOCTOR.create()));
        }

        // Finally, add logout button for all users
        addToDrawer(logoutButton);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        if ("fa".equals(event.getLocale().getLanguage())) {
            if (languageSelect != null) {
                languageSelect.setValue(persian);
            }
            UI.getCurrent().setDirection(Direction.RIGHT_TO_LEFT);
        } else {
            if (languageSelect != null) {
                languageSelect.setValue(english);
            }
            UI.getCurrent().setDirection(Direction.LEFT_TO_RIGHT);
        }
    }
}

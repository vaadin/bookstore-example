package org.vaadin.example.bookstore.ui.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.startup.ApplicationRouteRegistry;

import java.util.List;
import java.util.Optional;
import org.vaadin.example.bookstore.authentication.AccessControl;
import org.vaadin.example.bookstore.authentication.AccessControlFactory;

/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout implements BeforeEnterObserver {

    public static final String REDIRECT_PARAM = "redirect";

    private AccessControl accessControl;
    private String redirectPath;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // login form, centered in the available part of the screen
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Hint: same as username"));

        // layout to center login form when there is sufficient screen space
        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        // information text about logging in
        Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(centeringLayout);
    }

    private Component buildLoginInformation() {
        VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        H1 loginInfoHeader = new H1("Login Information");
        loginInfoHeader.setWidth("100%");
        Span loginInfoText = new Span(
                "Log in as \"admin\" to have full access. Log in with any " +
                        "other username to have read-only access. For all " +
                        "users, the password is same as the username.");
        loginInfoText.setWidth("100%");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);

        return loginInformation;
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            UI ui = getUI().get();
            ApplicationRouteRegistry registry = ApplicationRouteRegistry.getInstance(
                    VaadinService.getCurrent().getContext());
            Optional<Class<? extends Component>> navigationTarget =
                    registry.getNavigationTarget(redirectPath != null ? redirectPath : "");
            if (navigationTarget.isPresent()) {
                ui.navigate(navigationTarget.get());
            } else {
                ui.navigate("");
            }
        } else {
            event.getSource().setError(true);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<String> redirectParams = event.getLocation().getQueryParameters()
                .getParameters().get(REDIRECT_PARAM);
        if (redirectParams != null && !redirectParams.isEmpty()) {
            redirectPath = redirectParams.get(0);
        }
    }
}

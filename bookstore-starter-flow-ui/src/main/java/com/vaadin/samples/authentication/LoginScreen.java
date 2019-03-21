package com.vaadin.samples.authentication;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.samples.AdminView;
import com.vaadin.samples.MainLayout;

/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@HtmlImport("css/shared-styles.html")
public class LoginScreen extends FlexLayout {

    private TextField username;
    private PasswordField password;
    private Button login;
    private Button forgotPassword;
    private AccessControl accessControl;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
        username.focus();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // login form, centered in the available part of the screen
        Component loginForm = buildLoginForm();

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

    private Component buildLoginForm() {
        FormLayout loginForm = new FormLayout();

        // This is a workaround for current form-layout regression
        // https://github.com/vaadin/vaadin-form-layout/issues/108
        // which makes the login form is not shown in the middle of the screen.
        // Remove the following line after the issue gets fixed
        Div container = new Div();

        loginForm.setWidth("310px");

        loginForm.addFormItem(username = new TextField(), "Username");
        username.setWidth("15em");
        username.setValue("admin");
        username.setAutofocus(true);

        loginForm.add(new Html("<br/>"));
        loginForm.addFormItem(password = new PasswordField(), "Password");
        password.setWidth("15em");

        HorizontalLayout buttons = new HorizontalLayout();
        loginForm.add(new Html("<br/>"));
        loginForm.add(buttons);

        buttons.add(login = new Button("Login"));
        login.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        login.addClickListener(event -> login());
        login.addClickShortcut(Key.ENTER).listenOn(password);

        buttons.add(forgotPassword = new Button("Forgot password?"));
        forgotPassword.addClickListener(event -> showNotification(new Notification("Hint: same as username")));
        forgotPassword.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        container.add(loginForm);

        return container;
    }

    private Component buildLoginInformation() {
        VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        H1 loginInfoHeader = new H1("Login Information");
        Span loginInfoText = new Span(
                "Log in as \"admin\" to have full access. Log in with any " +
                        "other username to have read-only access. For all " +
                        "users, the password is same as the username.");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);

        return loginInformation;
    }

    private void login() {
        login.setEnabled(false);
        try {
            if (accessControl.signIn(username.getValue(), password.getValue())) {
                registerAdminViewIfApplicable();
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("Login failed. " +
                        "Please check your username and password and try again."));
                username.focus();
                password.setValue("");
            }
        } finally {
            login.setEnabled(true);
        }
    }

    private void registerAdminViewIfApplicable() {
        // register the admin view dynamically only for any admin user logged in
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            RouteConfiguration.forSessionScope().setRoute(AdminView.VIEW_NAME,
                    AdminView.class, MainLayout.class);
            // as logout will purge the session route registry, no need to
            // unregister the view on logout
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(3000);
        notification.open();
    }
}

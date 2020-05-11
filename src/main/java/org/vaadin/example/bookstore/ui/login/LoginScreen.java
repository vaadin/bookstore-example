package org.vaadin.example.bookstore.ui.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.bookstore.authentication.AccessControl;
import org.vaadin.example.bookstore.authentication.AccessControlFactory;
import org.vaadin.example.bookstore.ui.localization.LanguageSwitcher;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout {

    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    private AccessControl accessControl;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // login form, centered in the available part of the screen
        LoginForm loginForm = new LoginForm();
        loginForm.setI18n(createLoginI18n());
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show(resourceBundle.getString("login_hint")));

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

        H1 loginInfoHeader = new H1(resourceBundle.getString("login_info"));
        loginInfoHeader.setWidth("100%");
        Span loginInfoText = new Span(resourceBundle.getString("login_info_text"));
        loginInfoText.setWidth("100%");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);
        loginInformation.add(
                new LanguageSwitcher(Locale.ENGLISH,
                        new Locale("fa","IR", "فارسی")));

        return loginInformation;
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            getUI().get().navigate("");
        } else {
            event.getSource().setError(true);
        }
    }

    private LoginI18n createLoginI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getForm().setUsername(resourceBundle.getString("username"));
        i18n.getForm().setTitle(resourceBundle.getString("login"));
        i18n.getForm().setSubmit(resourceBundle.getString("login"));
        i18n.getForm().setPassword(resourceBundle.getString("password"));
        i18n.getForm().setForgotPassword(resourceBundle.getString("forgot_pass"));
        i18n.getErrorMessage().setTitle(resourceBundle.getString("login_error_title"));
        i18n.getErrorMessage().setMessage(resourceBundle.getString("login_error_msg"));
        return i18n;
    }
}

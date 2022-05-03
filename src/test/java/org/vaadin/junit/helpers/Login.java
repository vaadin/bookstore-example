package org.vaadin.junit.helpers;

import org.vaadin.example.bookstore.authentication.AccessControlFactory;

import com.vaadin.karibu.locator.LoginFormLocator;

/**
 * Helper class to login through the login form view that is shown on initial load.
 */
public class Login {

    /**
     * Login as a normal user
     */
    public static void login() {
        new LoginFormLocator().first().login("user","user");
    }

    /**
     * Login as an administrator
     */
    public static void loginAdmin() {
        new LoginFormLocator().first().login("admin","admin");
    }
}

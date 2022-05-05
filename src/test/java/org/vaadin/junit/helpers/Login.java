package org.vaadin.junit.helpers;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.testbench.ui.ComponentSearch;
import com.vaadin.testbench.ui.wrap.LoginFormWrap;

/**
 * Helper class to login through the login form view that is shown on initial
 * load.
 */
public class Login {

    /**
     * Login as a normal user
     */
    public static void login() {
        new LoginFormWrap(new ComponentSearch<>(LoginForm.class).first()
                .getComponent()).login("user", "user");
    }

    /**
     * Login as an administrator
     */
    public static void loginAdmin() {
        new LoginFormWrap(new ComponentSearch<>(LoginForm.class).first()
                .getComponent()).login("admin", "admin");
    }
}

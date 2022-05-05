package org.vaadin.junit;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.vaadin.junit.helpers.Login;
import org.vaadin.junit.helpers.MenuHelper;

import com.vaadin.flow.router.RouterLink;
import com.vaadin.testbench.ui.ComponentSearch;
import com.vaadin.testbench.ui.UITest;

/**
 * Test logging in with different users through the login page.
 */
public class LoginTest extends UITest {

    @Test
    public void loginUser_noAdminMenuItemAvailable() {
        Login.login();

        // Get all RouterLinks with the css className "menu-link"
        final List<RouterLink> menuLinks = new ComponentSearch<>(RouterLink.class).withClassName(
                "menu-link").all();

        final long admin = menuLinks.stream()
                .filter(link -> MenuHelper.getMenuItemText(link)
                        .equalsIgnoreCase("admin")).count();
        Assert.assertEquals(0, admin);
    }

    @Test
    public void loginAdmin_adminMenuItemAvailable() {
        Login.loginAdmin();

        final List<RouterLink> menuLinks = new ComponentSearch<>(RouterLink.class).withClassName(
                "menu-link").all();

        final long admin = menuLinks.stream()
                .filter(link -> MenuHelper.getMenuItemText(link)
                        .equalsIgnoreCase("admin")).count();
        Assert.assertEquals(1, admin);
    }

}

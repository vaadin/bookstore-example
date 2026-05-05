package org.vaadin.example.authentication;

import org.junit.jupiter.api.Assertions;
import org.vaadin.example.AbstractViewTest;
import org.vaadin.example.MainLayoutElement;

import com.vaadin.testbench.BrowserTest;

public class LoginScreenIT extends AbstractViewTest {

    @BrowserTest
    public void loginAsAdmin_hasAdminViewLink() {

        // when authenticating as admin
        $(LoginFormElement.class).single().login("admin", "admin");

        // then there is a link to admin's view
        Assertions.assertTrue(
                $(MainLayoutElement.class).single().hasMenuLink("admin"),
                "Expected link to admin view");
    }

    @BrowserTest
    public void loginAsUser_noAdminViewLink() {
        // when authenticating as a regular user
        $(LoginFormElement.class).single().login("user", "user");

        // then there is no link to admin's view
        Assertions.assertFalse(
                $(MainLayoutElement.class).single().hasMenuLink("admin"),
                "Expected no link to admin view");
    }
}

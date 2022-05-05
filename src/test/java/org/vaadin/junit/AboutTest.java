package org.vaadin.junit;

import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vaadin.example.bookstore.ui.MainLayout;
import org.vaadin.example.bookstore.ui.about.AboutView;
import org.vaadin.junit.helpers.Login;
import org.vaadin.junit.helpers.MenuHelper;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.Version;
import com.vaadin.testbench.ui.ComponentSearch;
import com.vaadin.testbench.ui.UITest;

public class AboutTest extends UITest {

    @BeforeEach
    public void login() {
        // for each test login as user
        Login.login();
    }

    @Test
    public void navigateToAboutViewWithRouterLink_showsFlowVersion() {
        // Find the menu link containing the text about and navigate to it.
        final Optional<RouterLink> about = new ComponentSearch(
                MainLayout.class).withFirst(RouterLink.class).all().stream()
                .filter(link -> MenuHelper.getMenuItemText((RouterLink) link)
                        .equalsIgnoreCase("about")).findFirst();
        navigate(about.get());

        MatcherAssert.assertThat("Expected version text",
                search(AboutView.class).withFirst(Span.class).last()
                        .getComponent().getText(), CoreMatchers.containsString(
                        "Vaadin version " + Version.getFullVersion()));

    }

    @Test
    public void navigateToAboutView_showsFlowVersion() {
        // Navigate by view class
        navigate(AboutView.class);

        MatcherAssert.assertThat("Expected version text",
                search(AboutView.class).withFirst(Span.class).last()
                        .getComponent().getText(), CoreMatchers.containsString(
                        "Vaadin version " + Version.getFullVersion()));

    }
}

package org.vaadin.example.about;


import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vaadin.example.AbstractViewTest;
import org.vaadin.example.MainLayoutElement;
import org.vaadin.example.authentication.LoginFormElement;

public class AboutViewIT extends AbstractViewTest {

    @Test
    public void openAboutView_showsFlowVersion() {

        // given authenticated as a regular user
        $(LoginFormElement.class).single().login("user", "password");

        // when selecting "About" from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).single();
        mainElem.clickMenuLink("About");

        // then the view contents a span with Flow version information
        final SpanElement aboutSpan = mainElem.$(HorizontalLayoutElement.class)
                .last().$(SpanElement.class).last();

        Assertions.assertTrue(
                aboutSpan.getText().contains("Vaadin"),
                "Expected version text");
    }
}

package org.vaadin.junit.helpers;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;

/**
 * Helper class to hide application implementation details on Menu.
 */
public class MenuHelper {
    /**
     * Get the link text for menu item router link.
     * The implementation hides the text inside a span inside the routerlink.
     *
     * @param link link to get text for
     * @return link text
     */
    public static String getMenuItemText(RouterLink link) {
        // This is application specific on how the RouterLink is populated with
        // an image and span containing text.
        return link.getChildren().filter(com -> com instanceof Span)
                .map(Span.class::cast).findFirst().get().getText();
    }

    public static String getMenuItemTest(Button button) {
        return button.getElement().getChildren()
                .filter(elem -> elem.isTextNode()).map(elem -> elem.getText())
                .findFirst().orElse(null);
    }
}

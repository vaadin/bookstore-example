package org.vaadin.example.bookstore.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ResourceBundle;

public class ConfirmDialog extends Dialog {

    private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    public ConfirmDialog(String caption, String text, String confirmButtonText,
            Runnable confirmListener) {

        final VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        add(content);

        add(new H3(caption));
        add(new Span(text));

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setPadding(false);
        add(buttons);

        final Button confirm = new Button(confirmButtonText, e -> {
            confirmListener.run();
            close();
        });
        confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttons.add(confirm);

        final Button cancel = new Button(resourceBundle.getString("cancel"), e -> close());
        buttons.add(cancel);

    }

}

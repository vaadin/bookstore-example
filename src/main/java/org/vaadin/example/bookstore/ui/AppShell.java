package org.vaadin.example.bookstore.ui;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

@PWA(name = "Bookstore", shortName = "Bookstore")
@StyleSheet(Lumo.STYLESHEET)
public class AppShell implements AppShellConfigurator {
}

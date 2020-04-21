package org.vaadin.example.bookstore.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

@PWA(name = "Bookstore", shortName = "Bookstore", enableInstallPrompt = false)
public class AppShell implements AppShellConfigurator {
}

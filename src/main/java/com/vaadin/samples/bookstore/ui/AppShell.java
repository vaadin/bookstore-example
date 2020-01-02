package com.vaadin.samples.bookstore.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.PWA;


@PWA(name = "Bookstore Example", shortName = "Bookstore")
@PageTitle("Bookstore")
public class AppShell implements AppShellConfigurator {
}

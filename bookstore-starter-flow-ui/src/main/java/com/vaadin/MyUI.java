package com.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;
import com.vaadin.samples.authentication.AccessControl;
import com.vaadin.samples.authentication.BasicAccessControl;

import javax.servlet.annotation.WebServlet;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
public class MyUI extends UI {

    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    }

    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

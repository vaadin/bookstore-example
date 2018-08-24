package com.vaadin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("My")
public class HelloWorldPage extends Div {
    public HelloWorldPage() {
        this.add(new H1("Hello World!"));
    }
}

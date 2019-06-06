package com.vaadin.samples.authentication;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-login-form")
public class LoginFormElement extends TestBenchElement {

    public void login(String username, String password) {
        $(TextFieldElement.class).id("vaadinLoginUsername")
                .setValue(username);
        $(PasswordFieldElement.class).id("vaadinLoginPassword")
                .setValue(password);
        $(ButtonElement.class).first()
                .click();
    }
}

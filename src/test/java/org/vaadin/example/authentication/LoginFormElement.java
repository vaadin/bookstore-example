package org.vaadin.example.authentication;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Element("vaadin-login-form")
public class LoginFormElement extends TestBenchElement {

    public void login(String username, String password) {
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("vaadinLoginUsername")));

        $(TextFieldElement.class).id("vaadinLoginUsername")
                .setValue(username);
        $(PasswordFieldElement.class).id("vaadinLoginPassword")
                .setValue(password);
        $(ButtonElement.class).first()
                .click();
    }
}

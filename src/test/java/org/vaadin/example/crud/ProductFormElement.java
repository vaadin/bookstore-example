package org.vaadin.example.crud;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.annotations.Attribute;
import com.vaadin.testbench.elementsbase.Element;

@Element("div")
@Attribute(name = "class", contains = "product-form")
public class ProductFormElement extends TestBenchElement {

    public TextFieldElement getProductNameElement() {
        return $(TextFieldElement.class).withLabel("Product name").single();
    }

    public ButtonElement getSaveButtonElement() {
        return $(ButtonElement.class).withCaption("Save").single();
    }

    public ButtonElement getCancelButtonElement() {
        return $(ButtonElement.class).withCaption("Cancel").single();
    }
}

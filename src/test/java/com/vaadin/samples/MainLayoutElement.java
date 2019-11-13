package com.vaadin.samples;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-app-layout")
public class MainLayoutElement extends TestBenchElement {

    public List<WebElement> findMenuLinks() {
        final List<WebElement> elements = new ArrayList<WebElement>();
        elements.addAll(findElements(By.className("menu-link")));
        elements.addAll(findElements(By.className("menu-button")));
        return elements;
    }

    public boolean hasMenuLink(String label) {
        return findMenuLinks().stream().anyMatch(elem -> elem.getText()
                .toLowerCase().equals(label.toLowerCase()));
    }

    public void clickMenuLink(String label) {
        findMenuLinks().stream()
                .filter(elem -> elem.getText().toLowerCase()
                        .equals(label.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No " + label))
                .click();
    }

}

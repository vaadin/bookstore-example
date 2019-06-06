package com.vaadin.samples;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.annotations.Attribute;
import com.vaadin.testbench.elementsbase.Element;

@Element("div")
@Attribute(name = "class", contains = "main-layout")
public class MainLayoutElement extends TestBenchElement {

    public List<WebElement> findMenuLinks() {
        return findElements(By.className("menu-link"));
    }

    public boolean hasMenuLink(String label) {
        return findMenuLinks().stream().anyMatch(elem ->
                elem.getText().equals(label));
    }

    public void clickMenuLink(String label) {
        findMenuLinks().stream().filter(elem -> elem.getText().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No " + label))
                .click();
    }
}

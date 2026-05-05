package org.vaadin.example.crud;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.loadtest.Destructive;

import org.vaadin.example.AbstractViewTest;
import org.vaadin.example.MainLayoutElement;
import org.vaadin.example.authentication.LoginFormElement;

public class SampleCrudViewIT extends AbstractViewTest {

    @BrowserTest
    public void userSelectsProduct_cannotEditProductInformation() {

        // given authenticated as a regular user
        $(LoginFormElement.class).single().login("user", "user");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainLayout = $(MainLayoutElement.class).single();
        mainLayout.clickMenuLink("Inventory");

        // when selecting an item from the product grid
        final GridElement grid = $(GridElement.class).single();
        grid.getCell(0, 0).click();

        // then the product data is not editable
        Assertions.assertTrue(
                findElements(By.className("product-form")).isEmpty(),
                "Product form should not be visible");
    }

    @Destructive
    @BrowserTest
    public void adminSelectsProduct_canUpdateProductInformation() {

        // given authenticated as an admin
        $(LoginFormElement.class).single().login("admin", "admin");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).first();
        mainElem.clickMenuLink("Inventory");

        // when selecting an item from the product grid
        final GridElement grid = $(GridElement.class).single();
        grid.getCell(0, 0).click();

        // when altering the product name and clicking the save button
        final ProductFormElement prodForm = $(ProductFormElement.class).single();
        final String newTitle = "Cronan's Guide to Nanomixology";
        prodForm.getProductNameElement().setValue(newTitle);
        prodForm.getSaveButtonElement().click();

        // then the grid cell is updated to the new title
        Assertions.assertEquals(newTitle,
                grid.getCell(0, 0).getText(), "Title in grid not updated");
    }

    @Destructive
    @BrowserTest
    public void adminCreatesNewProduct_productIsAvailableInGird() {
        // given authenticated as an admin
        $(LoginFormElement.class).single().login("admin", "admin");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).single();
        mainElem.clickMenuLink("Inventory");

        // when clicking the "New product" button
        $(ButtonElement.class).withAttribute("theme", "primary").single().click();

        // when entering new product data and saving the product
        final ProductFormElement prodForm = $(ProductFormElement.class).single();
        final String newTitle = "Cronan's Guide to Nanomixology, 2nd ed.";
        prodForm.getProductNameElement().setValue(newTitle);
        prodForm.getSaveButtonElement().click();

        // then the new title is in the grid
        final GridElement grid = $(GridElement.class).single();
        final boolean foundInGrid = IntStream.range(0, grid.getRowCount())
                .mapToObj(row -> grid.getCell(row, 0).getText())
                .anyMatch(newTitle::equals);
        Assertions.assertTrue(foundInGrid, "Title not found in grid");
    }
}

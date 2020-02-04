package org.vaadin.example.crud;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import org.vaadin.example.AbstractViewTest;
import org.vaadin.example.MainLayoutElement;
import org.vaadin.example.authentication.LoginFormElement;

public class SampleCrudViewIT extends AbstractViewTest {

    @Test
    public void userSelectsProduct_cannotEditProductInformation() {

        // given authenticated as a regular user
        $(LoginFormElement.class).first().login("user", "user");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainLayout = $(MainLayoutElement.class).first();
        mainLayout.clickMenuLink("Inventory");

        // when selecting an item from the product grid
        final GridElement grid = $(GridElement.class).first();
        grid.getCell(0, 0).click();

        // then the product data is not editable
        Assert.assertTrue("Product form should not be visible",
                findElements(By.className("product-form")).isEmpty());
    }

    @Test
    public void adminSelectsProduct_canUpdateProductInformation() {

        // given authenticated as an admin
        $(LoginFormElement.class).first().login("admin", "admin");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).first();
        mainElem.clickMenuLink("Inventory");

        // when selecting an item from the product grid
        final GridElement grid = $(GridElement.class).first();
        grid.getCell(0, 0).click();

        // when altering the product name and clicking the save button
        final ProductFormElement prodForm = $(ProductFormElement.class).first();
        final String newTitle = "Cronan's Guide to Nanomixology";
        prodForm.getProductNameElement().setValue(newTitle);
        prodForm.getSaveButtonElement().click();

        // then the grid cell is updated to the new title
        Assert.assertEquals("Title in grid not updated", newTitle,
                grid.getCell(0, 0).getText());
    }

    @Test
    public void adminCreatesNewProduct_productIsAvailableInGird() {
        // given authenticated as an admin
        $(LoginFormElement.class).first().login("admin", "admin");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).first();
        mainElem.clickMenuLink("Inventory");

        // when clicking the "New product" button
        $(ButtonElement.class).attribute("theme", "primary").first().click();

        // when entering new product data and saving the product
        final ProductFormElement prodForm = $(ProductFormElement.class).first();
        final String newTitle = "Cronan's Guide to Nanomixology, 2nd ed.";
        prodForm.getProductNameElement().setValue(newTitle);
        prodForm.getSaveButtonElement().click();

        // then the new title is in the grid
        final GridElement grid = $(GridElement.class).first();
        final boolean foundInGrid = IntStream.range(0, grid.getRowCount())
                .mapToObj(row -> grid.getCell(row, 0).getText())
                .anyMatch(title -> newTitle.equals(title));
        Assert.assertTrue("Title not found in grid", foundInGrid);
    }
}

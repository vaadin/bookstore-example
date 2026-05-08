package org.vaadin.example.crud;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.loadtest.Destructive;

import org.vaadin.example.AbstractViewTest;
import org.vaadin.example.MainLayoutElement;
import org.vaadin.example.authentication.LoginFormElement;

public class SampleCrudViewIT extends AbstractViewTest {

    @Test
    public void userSelectsProduct_cannotEditProductInformation() {

        // given authenticated as a regular user
        $(LoginFormElement.class).single().login("user", "password");

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

    @Test
    public void userScrollsPageDownAndUp() {

        // given authenticated as a regular user
        $(LoginFormElement.class).single().login("user", "password");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainLayout = $(MainLayoutElement.class).single();
        mainLayout.clickMenuLink("Inventory");

        // when selecting an item from the product grid
        final GridElement grid = $(GridElement.class).single();

        String rowOneProductName = grid.getCell(0, 0).getText();
        Assertions.assertNotNull(rowOneProductName);
        Assertions.assertNotEquals("", rowOneProductName);

        // scroll down to trigger fetch
        grid.scrollToRow(1000);

        Assertions.assertNotNull(grid.getCell(200, 0).getText());
        Assertions.assertNotEquals("", grid.getCell(200, 0).getText());

        // scroll back up
        grid.scrollToRow(0);
        Assertions.assertEquals(rowOneProductName, grid.getCell(0, 0).getText());
    }

    @Test
    public void adminSelectsProduct_cancelEdits() {

        // given authenticated as an admin
        $(LoginFormElement.class).single().login("admin", "password");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).single();
        mainElem.clickMenuLink("Inventory");

        // when selecting an item from the product grid
        final GridElement grid = $(GridElement.class).single();
        grid.getCell(0, 0).click();
        String rowOneProductName = grid.getCell(0, 0).getText();

        // when altering the product name and clicking the cancel button
        final ProductFormElement prodForm = $(ProductFormElement.class).single();
        prodForm.getProductNameElement().setValue("Cronan's Guide to Nanomixology");
        prodForm.getCancelButtonElement().click();

        // then grid cell stays unchanged
        Assertions.assertEquals(rowOneProductName,
                grid.getCell(0, 0).getText(), "Title in grid was updated after canceling");
    }

    // skip load test since edit changes behavior for the following VUs
    @Destructive
    @Test
    public void adminSelectsProduct_canUpdateProductInformation() {

        // given authenticated as an admin
        $(LoginFormElement.class).single().login("admin", "password");

        // given "Inventory" is selected from the sidebar menu
        final MainLayoutElement mainElem = $(MainLayoutElement.class).single();
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

    // skip load test since edit changes behavior for the following VUs
    @Destructive
    @Test
    public void adminCreatesNewProduct_productIsAvailableInGrid() {
        // given authenticated as an admin
        $(LoginFormElement.class).single().login("admin", "password");

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

        // filter by new title
        TextFieldElement filterField = $(TextFieldElement.class)
                .withId("grid-filter").single();
        filterField.setValue(newTitle);

        // then the new title is in the grid
        final GridElement grid = $(GridElement.class).single();
        final boolean foundInGrid = IntStream.range(0, grid.getRowCount())
                .mapToObj(row -> grid.getCell(row, 0).getText())
                .anyMatch(newTitle::equals);
        Assertions.assertTrue(foundInGrid, "Title not found in grid");
    }
}

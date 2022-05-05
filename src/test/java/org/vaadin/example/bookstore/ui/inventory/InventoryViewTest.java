package org.vaadin.example.bookstore.ui.inventory;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.vaadin.junit.helpers.Login;

import com.vaadin.testbench.ui.UITest;
import com.vaadin.testbench.ui.wrap.ButtonWrap;
import com.vaadin.testbench.ui.wrap.GridWrap;
import com.vaadin.testbench.ui.wrap.TextFieldWrap;

class InventoryViewTest extends UITest {

    @Test
    public void userSelectsProduct_cannotEditProductInformation() {
        Login.login();
        InventoryView view = navigate(InventoryView.class);

        // Can we get wrap to return the expected wrap implementation
        final GridWrap grid = (GridWrap) $(view.grid);
        grid.clickRow(0);

        Assert.assertFalse("Product form should not be visible",
                view.form.isVisible());
    }

    @Test
    public void adminSelectsProduct_canUpdateProductInformation() {
        Login.loginAdmin();
        InventoryView view = navigate(InventoryView.class);

        GridWrap grid = (GridWrap) $(view.grid);
        grid.clickRow(0);

        Assert.assertTrue("Product form should be visible",
                view.form.isVisible());

        final String newTitle = "Cronan's Guide to Nanomixology";
        TextFieldWrap tf = (TextFieldWrap) $(view.form.productName);
        tf.setValue(newTitle);
        ButtonWrap button = (ButtonWrap) $(view.form.save);
        button.click();

        Assert.assertEquals(newTitle, grid.getCellText(0, 0));
    }

    @Test
    public void adminCreatesNewProduct_productIsAvailableInGird() {
        Login.loginAdmin();
        InventoryView view = navigate(InventoryView.class);

        ((ButtonWrap) $(view.newProduct)).click();

        Assert.assertTrue("Product form should be visible",
                view.form.isVisible());

        final String newTitle = "Cronan's Guide to Nanomixology";
        ((TextFieldWrap) $(view.form.productName)).setValue(newTitle);
        ((ButtonWrap) $(view.form.save)).click();

        GridWrap grid = (GridWrap) $(view.grid);
        final boolean foundInGrid = IntStream.range(0, grid.size())
                .mapToObj(row -> grid.getCellText(row, 0))
                .anyMatch(title -> newTitle.equals(title));

        Assert.assertTrue("Title not found in grid", foundInGrid);
    }
}

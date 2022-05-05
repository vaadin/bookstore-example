package org.vaadin.junit;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.vaadin.example.bookstore.ui.inventory.InventoryView;
import org.vaadin.example.bookstore.ui.inventory.ProductForm;
import org.vaadin.junit.helpers.Login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.testbench.ui.ComponentSearch;
import com.vaadin.testbench.ui.UITest;
import com.vaadin.testbench.ui.wrap.ButtonWrap;
import com.vaadin.testbench.ui.wrap.GridWrap;
import com.vaadin.testbench.ui.wrap.TextFieldWrap;

/**
 * Test the inventory view for adding and editing content for admin, and
 * view only for normal user.
 */
public class InventoryTest extends UITest {

    @Test
    public void userSelectsProduct_cannotEditProductInformation() {
        Login.login();
        navigate(InventoryView.class);

        final GridWrap grid_ = search(Grid.class).first();
        // Click on first grid row
        grid_.clickRow(0);

        Assert.assertTrue("Product form should not be visible",
                search(ProductForm.class).all().isEmpty());
    }

    @Test
    public void adminSelectsProduct_canUpdateProductInformation() {
        Login.loginAdmin();
        navigate(InventoryView.class);

        final GridWrap locator = search(Grid.class).first();
        locator.clickRow(0);

        Assert.assertFalse("Product form should be visible",
                search(ProductForm.class).all().isEmpty());

        final ComponentSearch<ProductForm> form = search(ProductForm.class);

        final String newTitle = "Cronan's Guide to Nanomixology";
        // get TextField in ProductForm. This will not find a field with the
        // same caption outside of the form
        ((TextFieldWrap) form.withFirst(TextField.class)
                .withCaption("Product name").first()).setValue(newTitle);
        ((ButtonWrap) form.withFirst(Button.class).withCaption("Save")
                .first()).click();

        Assert.assertEquals(newTitle, locator.getCellText(0, 0));
    }

        @Test
        public void adminCreatesNewProduct_productIsAvailableInGird() {
            Login.loginAdmin();
            navigate(InventoryView.class);

            ((ButtonWrap)search(Button.class).withCaption("New product").first()).click();

            final ComponentSearch<ProductForm> form = search(ProductForm.class);
            final String newTitle = "Cronan's Guide to Nanomixology, 2nd ed.";
            ((TextFieldWrap)form.withFirst(TextField.class).withCaption("Product name").first()).setValue(newTitle);
            ((ButtonWrap)form.withFirst(Button.class).withCaption("Save").first()).click();

            final GridWrap locator = search(Grid.class).first();

            final boolean foundInGrid = IntStream.range(0, locator.size())
                    .mapToObj(row -> locator.getCellText(row, 0))
                    .anyMatch(title -> newTitle.equals(title));

            Assert.assertTrue("Title not found in grid", foundInGrid);
        }
}

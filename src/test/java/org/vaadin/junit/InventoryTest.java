package org.vaadin.junit;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.vaadin.example.bookstore.authentication.AccessControlFactory;
import org.vaadin.example.bookstore.ui.inventory.InventoryView;
import org.vaadin.example.bookstore.ui.inventory.ProductForm;
import org.vaadin.junit.helpers.Login;

import com.vaadin.karibu.KaribuTest;
import com.vaadin.karibu.locator.ButtonLocator;
import com.vaadin.karibu.locator.ComponentLocator;
import com.vaadin.karibu.locator.GridLocator;
import com.vaadin.karibu.locator.TextFieldLocator;

/**
 * Test the inventory view for adding and editing content for admin, and
 * view only for normal user.
 */
public class InventoryTest extends KaribuTest {

    @Test
    public void userSelectsProduct_cannotEditProductInformation() {
        Login.login();
        navigate(InventoryView.class);

        final GridLocator locator = $(GridLocator.class).first();
        // Click on first grid row
        locator.clickRow(0);

        Assert.assertTrue("Product form should not be visible",
                £(ProductForm.class).all().isEmpty());
    }

    @Test
    public void adminSelectsProduct_canUpdateProductInformation() {
        Login.loginAdmin();
        navigate(InventoryView.class);

        final GridLocator locator = $(GridLocator.class).first();
        locator.clickRow(0);

        Assert.assertFalse("Product form should be visible",
                £(ProductForm.class).all().isEmpty());

        final ComponentLocator<ProductForm> form = £(ProductForm.class).first();

        final String newTitle = "Cronan's Guide to Nanomixology";
        // get TextField in ProductForm. This will not find a field with the
        // same caption outside of the form
        form.$(TextFieldLocator.class).withCaption("Product name").getComponent().setValue(newTitle);
        form.$(ButtonLocator.class).withCaption("Save").click();

        Assert.assertEquals(newTitle, locator.getCellText(0,0));
    }



    @Test
    public void adminCreatesNewProduct_productIsAvailableInGird() {
        Login.loginAdmin();
        navigate(InventoryView.class);

        $(ButtonLocator.class).withCaption("New product").first().click();

        final ComponentLocator<ProductForm> form = £(ProductForm.class).first();
        final String newTitle = "Cronan's Guide to Nanomixology, 2nd ed.";
        form.$(TextFieldLocator.class).withCaption("Product name").getComponent().setValue(newTitle);
        form.$(ButtonLocator.class).withCaption("Save").click();

        final GridLocator locator = $(GridLocator.class).first();

        final boolean foundInGrid = IntStream.range(0, locator.size())
                .mapToObj(row -> locator.getCellText(row, 0))
                .anyMatch(title -> newTitle.equals(title));

        Assert.assertTrue("Title not found in grid", foundInGrid);
    }
}

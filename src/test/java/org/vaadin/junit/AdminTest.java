package org.vaadin.junit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.vaadin.example.bookstore.ui.AdminView;
import org.vaadin.junit.helpers.Login;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.karibu.KaribuTest;
import com.vaadin.karibu.locator.ButtonLocator;

public class AdminTest extends KaribuTest {

    @Test
    public void loggedInAsAdmin_adminViewIsAvailable_categoryCanBeAdded() {
        Login.loginAdmin();

        navigate(AdminView.class);
        Assert.assertEquals("Hello Admin",
                £(H2.class).first().getComponent().getText());

        final DataProvider listDataProvider = £(IronList.class).first()
                .getComponent().getDataProvider();
        int size = listDataProvider.size(new Query<>());

        $(ButtonLocator.class).withCaption("Add New Category").first().click();

        Assert.assertEquals(size+1, listDataProvider.size(new Query<>()));
    }

}

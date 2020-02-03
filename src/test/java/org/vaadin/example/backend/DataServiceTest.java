package org.vaadin.example.backend;

import org.junit.Before;
import org.junit.Test;

import org.vaadin.example.bookstore.backend.DataService;
import org.vaadin.example.bookstore.backend.data.Product;
import org.vaadin.example.bookstore.backend.mock.MockDataService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Simple unit test for the back-end data service.
 */
public class DataServiceTest {

    private DataService service;

    @Before
    public void setUp() throws Exception {
        service = MockDataService.getInstance();
    }

    @Test
    public void testDataServiceCanFetchProducts() throws Exception {
        assertFalse(service.getAllProducts().isEmpty());
    }

    @Test
    public void testDataServiceCanFetchCategories() throws Exception {
        assertFalse(service.getAllCategories().isEmpty());
    }

    @Test
    public void testUpdateProduct_updatesTheProduct() throws Exception {
        Product p = service.getAllProducts().iterator().next();
        p.setProductName("My Test Name");
        service.updateProduct(p);
        Product p2 = service.getAllProducts().iterator().next();
        assertEquals("My Test Name", p2.getProductName());
    }
}

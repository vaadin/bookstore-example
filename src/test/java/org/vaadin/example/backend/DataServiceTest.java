package org.vaadin.example.backend;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vaadin.example.bookstore.backend.data.Product;
import org.vaadin.example.bookstore.backend.services.DataService;
import org.vaadin.example.bookstore.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Simple unit test for the back-end data service.
 */
@SpringBootTest(classes = Application.class)
public class DataServiceTest {

    @Autowired
    private DataService service;

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
        Product p2 = service.getProductById(p.getId());
        assertEquals("My Test Name", p2.getProductName());
    }
}

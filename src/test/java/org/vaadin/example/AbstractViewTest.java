package org.vaadin.example;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.vaadin.testbench.BrowserTestBase;
import com.vaadin.testbench.loadtest.LoadTestItHelper;

/**
 * Base class for ITs
 * <p>
 * The tests use Chrome driver (see pom.xml for integration-tests profile) to
 * run integration tests on a headless Chrome.
 */
public abstract class AbstractViewTest extends BrowserTestBase {

    private final String route;

    public AbstractViewTest() {
        this("");
    }

    protected AbstractViewTest(String route) {
        this.route = route;
    }

    @BeforeEach
    public void setBrowserTestInfo() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless");
        }
        LoadTestItHelper.openWithProxy(new ChromeDriver(options),
                LoadTestItHelper.getRootURL() + "/" + route);
    }

}

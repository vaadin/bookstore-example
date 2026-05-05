package org.vaadin.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.vaadin.testbench.AbstractBrowserDriverTestBase;
import com.vaadin.testbench.loadtest.LoadTestItHelper;

/**
 * Base class for ITs
 * <p>
 * The tests use Chrome driver (see pom.xml for integration-tests profile) to
 * run integration tests on a headless Chrome.
 */
public abstract class AbstractViewTest extends AbstractBrowserDriverTestBase {

    private final String route;

    public AbstractViewTest() {
        this("");
    }

    protected AbstractViewTest(String route) {
        this.route = route;
    }

    @BeforeEach
    public void open() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        setDriver(LoadTestItHelper.openWithProxy(new ChromeDriver(options),
                LoadTestItHelper.getRootURL() + "/" + route));
    }

    @AfterEach
    public void quit() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}

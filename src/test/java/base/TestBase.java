package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;

import util.DriverFactory;
import com.cucumberbdd.pageobjects.WaitUtilsProvider;

public class TestBase {
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.initDriver();
        // initialize WaitUtilsProvider for current thread
        WaitUtilsProvider.init(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // remove thread-local WaitUtils and quit driver
        WaitUtilsProvider.remove();
        DriverFactory.quitDriver();
    }
}
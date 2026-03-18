package util;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    private DriverFactory() {}

    public static WebDriver initDriver() {
        if (tlDriver.get() == null) {
            String browser = ConfigReader.get("browser");
            if (browser == null) browser = "chrome";
            WebDriver driver;
            switch (browser.toLowerCase()) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    // enable headless by setting headless=true in config if needed
                    if ("true".equalsIgnoreCase(ConfigReader.get("headless"))) {
                        options.addArguments("--headless=new");
                    }
                    driver = new ChromeDriver(options);
            }

            // Do NOT set implicit wait here; rely on explicit waits via WaitUtils
            int pageLoad = ConfigReader.getInt("page.load.timeout", 30);
            if (pageLoad > 0) {
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
            }
            tlDriver.set(driver);
        }
        return tlDriver.get();
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
            tlDriver.remove();
        }
    }
}
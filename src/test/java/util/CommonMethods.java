package util;

import java.time.Duration;
import java.time.Instant;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.cucumberbdd.pageobjects.WaitUtilsProvider;

/**
 * Test-scoped CommonMethods with assertions that depend on TestNG.
 * This is the single recommended CommonMethods class for tests and step definitions.
 */
public class CommonMethods {

    private static final ThreadLocal<SoftAssert> softAssertTL = ThreadLocal.withInitial(SoftAssert::new);

    // Delegates to WaitUtilsProvider for wait helpers
    public static WebElement waitForVisible(WebElement element) {
        return WaitUtilsProvider.get().waitForVisible(element);
    }

    public static WebElement waitForClickable(WebElement element) {
        return WaitUtilsProvider.get().waitForClickable(element);
    }

    public static boolean waitForElementToDisappear(By locator) {
        return WaitUtilsProvider.get().waitForElementToDisappear(locator);
    }

    // Convenience to get the thread's driver (from DriverFactory)
    public static WebDriver getDriver() {
        return util.DriverFactory.getDriver();
    }

    // Screenshot helper
    public static String takeScreenshot(String prefix) {
        WebDriver driver = getDriver();
        if (driver == null) return null;
        return ScreenshotUtil.takeScreenshot(driver, prefix);
    }

    // Retry click with simple backoff
    public static void retryClick(WebElement element, int attempts, long waitBetweenMs) {
        WebDriver driver = getDriver();
        int tries = 0;
        while (true) {
            try {
                waitForClickable(element);
                element.click();
                return;
            } catch (Exception e) {
                tries++;
                if (tries >= attempts) throw new RuntimeException("retryClick failed after attempts", e);
                try {
                    Thread.sleep(waitBetweenMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
    }

    // Wait for AJAX requests to finish (uses JS readyState + jQuery if available)
    public static void waitForAjax(Duration timeout) {
        WebDriver driver = getDriver();
        if (driver == null) return;
        Instant end = Instant.now().plus(timeout);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (Instant.now().isBefore(end)) {
            try {
                Boolean jqueryActive = true;
                try {
                    Object jq = js.executeScript("return window.jQuery != undefined && jQuery.active == 0;");
                    if (jq instanceof Boolean) jqueryActive = (Boolean) jq;
                } catch (Exception ignored) {
                    // jQuery not present — ignore
                }

                Boolean ready = (Boolean) js.executeScript("return document.readyState === 'complete'");
                if (ready && jqueryActive) return;
            } catch (Exception e) {
                // ignore and retry until timeout
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new RuntimeException("waitForAjax timed out after " + timeout.toString());
    }

    // Wait for stale element to be refreshed/removed
    public static void waitForStaleElement(WebElement element, Duration timeout) {
        Instant end = Instant.now().plus(timeout);
        while (Instant.now().isBefore(end)) {
            try {
                // calling any method will throw StaleElementReferenceException if stale
                element.isEnabled();
                // if no exception, element is still attached
            } catch (StaleElementReferenceException sere) {
                return; // element became stale
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new RuntimeException("Element did not become stale within timeout: " + timeout.toString());
    }

    // Hard assertions
    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertElementVisible(WebElement element, String message) {
        waitForVisible(element);
        Assert.assertTrue(element.isDisplayed(), message);
    }

    // Soft assertions
    public static SoftAssert softAssert() {
        return softAssertTL.get();
    }

    public static void softAssertAll() {
        try {
            softAssert().assertAll();
        } finally {
            softAssertTL.remove();
        }
    }
}

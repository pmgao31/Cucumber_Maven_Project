package com.cucumberbdd.pageobjects;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
    private WebDriver driver;
    private Duration defaultTimeout = Duration.ofSeconds(10);
    private Duration pollingInterval = Duration.ofMillis(500);

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    public void setDefaultTimeout(Duration timeout) {
        this.defaultTimeout = timeout;
    }

    public void setPollingInterval(Duration polling) {
        this.pollingInterval = polling;
    }

    // Explicit wait for visibility
    public WebElement waitForVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Explicit wait for clickable
    public WebElement waitForClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // Wait for text to be present in element
    public boolean waitForText(WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // Wait for presence by locator
    public WebElement waitForPresence(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // Wait for element to disappear (by element)
    public boolean waitForElementToDisappear(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    // Wait for element to disappear (by locator)
    public boolean waitForElementToDisappear(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Wait for attribute to be a specific value
    public boolean waitForAttribute(WebElement element, String attribute, String value) {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    // Fluent wait generic
    public <T> T fluentWait(Function<WebDriver, T> condition) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(defaultTimeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);
        return wait.until(condition);
    }

    // Wait for page load complete
    public void waitForPageLoadComplete() {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(defaultTimeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);

        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        wait.until(pageLoadCondition);
    }

    // Scroll element into view
    public void scrollIntoView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // JS click as fallback (scrolls into view first)
    public void jsClick(WebElement element) {
        try {
            // prefer normal click when possible
            waitForClickable(element);
            element.click();
        } catch (Exception e) {
            try {
                scrollIntoView(element);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
            } catch (Exception ex) {
                // last resort: rethrow original exception
                throw new RuntimeException("Unable to click element via JS or normal click", ex);
            }
        }
    }
}
package com.cucumberbdd.pageobjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class BasePage {
    protected WebDriver driver;
    private final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Initialize thread-local WaitUtils for the current thread if driver is available
        if (this.driver != null) {
            WaitUtilsProvider.init(driver);
        }
    }

    protected void click(WebElement element) {
        WaitUtils waits = WaitUtilsProvider.get();
        waits.jsClick(element);
    }

    protected void sendKeys(WebElement element, String text) {
        WaitUtils waits = WaitUtilsProvider.get();
        waits.waitForVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        WaitUtils waits = WaitUtilsProvider.get();
        waits.waitForVisible(element);
        return element.getText();
    }

    protected void waitForVisible(WebElement element) {
        WaitUtils waits = WaitUtilsProvider.get();
        waits.waitForVisible(element);
    }

    protected void selectByIndex(WebElement ele, int index) {
        WaitUtils waits = WaitUtilsProvider.get();
        waits.waitForVisible(ele);
        new Select(ele).selectByIndex(index);
    }
}
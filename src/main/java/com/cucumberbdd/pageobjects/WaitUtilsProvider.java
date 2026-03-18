package com.cucumberbdd.pageobjects;

import org.openqa.selenium.WebDriver;

public class WaitUtilsProvider {
    private static final ThreadLocal<WaitUtils> threadLocal = new ThreadLocal<>();

    private WaitUtilsProvider() {
        // prevent instantiation
    }

    public static synchronized void init(WebDriver driver) {
        WaitUtils current = threadLocal.get();
        if (current != null) {
            try {
                // best-effort check: compare driver reference by reflection not available; we rely on caller to pass same driver
                // If another WaitUtils exists, assume it's fine and return
                return;
            } catch (Exception e) {
                // fallback: replace
                threadLocal.remove();
                threadLocal.set(new WaitUtils(driver));
                return;
            }
        }
        threadLocal.set(new WaitUtils(driver));
    }

    public static WaitUtils get() {
        WaitUtils utils = threadLocal.get();
        if (utils == null) {
            throw new IllegalStateException("WaitUtils not initialized for this thread. Call WaitUtilsProvider.init(driver) first.");
        }
        return utils;
    }

    public static void remove() {
        threadLocal.remove();
    }
}
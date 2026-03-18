package util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private final int maxRetries;

    public RetryAnalyzer() {
        this.maxRetries = ConfigReader.getInt("retry.count", 1);
    }

    @Override
    public boolean retry(ITestResult result) {
        if (count >maxRetries) return false;
        Throwable t = result.getThrowable();
        if (t == null) return false;
        if (isRetryable(t)) {
            count++;
            int waitMs = ConfigReader.getInt("retry.backoff.ms", 500);
            // log retry attempt
            String testName = result.getMethod().getMethodName();
            String msg = String.format("Retrying test '%s' (attempt %d/%d) due to: %s", testName, count, maxRetries, t.toString());
            System.out.println(msg);
            try {
                if (util.ExtentTestManager.getTest() != null) {
                    util.ExtentTestManager.getTest().info(msg);
                }
            } catch (Throwable ignore) {
                // best-effort logging
            }

            try {
                Thread.sleep(waitMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        }
        return false;
    }

    private boolean isRetryable(Throwable t) {
        if (t instanceof org.openqa.selenium.NoSuchElementException) return true;
        if (t instanceof org.openqa.selenium.StaleElementReferenceException) return true;
        if (t instanceof org.openqa.selenium.ElementClickInterceptedException) return true;
        if (t instanceof org.openqa.selenium.WebDriverException) return true;
        String cname = t.getClass().getName().toLowerCase();
        if (cname.contains("timeout")) return true;
        if (cname.contains("stale")) return true;
        if (cname.contains("element")) return true;
        return false;
    }
}
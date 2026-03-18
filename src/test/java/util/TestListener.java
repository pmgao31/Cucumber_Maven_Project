package util;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // nothing specific for suite start
    }

    @Override
    public void onFinish(ITestContext context) {
        // Create a suite-level summary in the Extent report: totals and duration
        try {
            // Collect all test attempts (passed/failed/skipped). Use latest attempt per unique test
            Set<ITestResult> allAttempts = new HashSet<>();
            allAttempts.addAll(context.getPassedTests().getAllResults());
            allAttempts.addAll(context.getFailedTests().getAllResults());
            allAttempts.addAll(context.getSkippedTests().getAllResults());

            Map<String, ITestResult> finalResults = new HashMap<>();
            for (ITestResult r : allAttempts) {
                String key = r.getMethod().getTestClass().getName() + "#" + r.getMethod().getMethodName();
                Object[] params = r.getParameters();
                if (params != null && params.length > 0) {
                    key += ":" + Arrays.deepHashCode(params);
                }
                ITestResult existing = finalResults.get(key);
                long rTime = r.getEndMillis() > 0 ? r.getEndMillis() : r.getStartMillis();
                if (existing == null) {
                    finalResults.put(key, r);
                } else {
                    long existingTime = existing.getEndMillis() > 0 ? existing.getEndMillis() : existing.getStartMillis();
                    // Keep the attempt with the latest time (final attempt)
                    if (rTime > existingTime) {
                        finalResults.put(key, r);
                    }
                }
            }

            int passed = 0;
            int failed = 0;
            int skipped = 0;
            for (ITestResult r : finalResults.values()) {
                int status = r.getStatus();
                if (status == ITestResult.SUCCESS) passed++;
                else if (status == ITestResult.FAILURE) failed++;
                else if (status == ITestResult.SKIP) skipped++;
            }
            int total = finalResults.size();

            Date start = context.getStartDate();
            Date end = context.getEndDate();
            long durationMs = 0L;
            if (start != null && end != null) {
                durationMs = end.getTime() - start.getTime();
            }
            String durationStr = formatDuration(durationMs);
            double passPercent = total > 0 ? (passed * 100.0d / total) : 0.0d;

            ExtentTest summary = ExtentManager.getExtentReports().createTest("Suite Summary");
            summary.info("Total tests: " + total);
            summary.info("Passed: " + passed);
            summary.info("Failed: " + failed);
            summary.info("Skipped: " + skipped);
            summary.info(String.format("Pass percentage: %.2f%%", passPercent));
            summary.info("Total time: " + durationStr + " (HH:MM:SS)");

        } catch (Exception e) {
            // best-effort; do not fail the suite because of reporting
            ExtentManager.getExtentReports().createTest("Suite Summary").warning("Failed to produce summary: " + e.getMessage());
        }

        // flush reports at the end of suite
        ExtentManager.getExtentReports().flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
//        ensureTestExists(result);
//        String displayName = getDisplayName(result);
//        ExtentTestManager.getTest().log(Status.INFO, "Test started: " + displayName + " at " + Instant.now().toString());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
//        ensureTestExists(result);
//        String step = ScenarioContext.getCurrentStep();
//        ExtentTestManager.getTest().pass("Test passed: " + (step != null ? step : "(no step)") + " at " + Instant.now().toString());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            ensureTestExists(result);
            String screenshot = null;
            try {
                String scen = ScenarioContext.getScenarioName();
                String step = ScenarioContext.getCurrentStep();
                String caption = "failure" + (scen != null ? "_" + sanitize(scen) : "") + (step != null ? "_" + sanitize(step) : "");
                screenshot = ScreenshotUtil.takeScreenshot(util.DriverFactory.getDriver(), caption + "_");
            } catch (Exception e) {
                // ignore screenshot failures
            }

            if (screenshot != null) {
                ExtentTestManager.getTest().fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
            } else {
                ExtentTestManager.getTest().fail(result.getThrowable());
            }
            ExtentTestManager.getTest().log(Status.FAIL, "Failure at " + Instant.now().toString());
        } catch (Exception e) {
            // fallback logging
            if (ExtentTestManager.getTest() != null) {
                ExtentTestManager.getTest().fail("Test failed and error capturing screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
//        ensureTestExists(result);
//        Throwable t = result.getThrowable();
//        if (t != null) {
//            ExtentTestManager.getTest().skip(t);
//        } else {
//            ExtentTestManager.getTest().skip("Test skipped: " + result.getMethod().getMethodName() + " at " + Instant.now().toString());
//        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // not used
    }

    private String sanitize(String s) {
        return s == null ? "" : s.replaceAll("[^a-zA-Z0-9-_]", "_");
    }

    private String getDisplayName(ITestResult result) {
        String scenario = ScenarioContext.getScenarioName();
        return scenario != null ? scenario : result.getMethod().getMethodName();
    }

    private void ensureTestExists(ITestResult result) {
        if (ExtentTestManager.getTest() == null) {
            String displayName = getDisplayName(result);
            ExtentTestManager.setTest(ExtentManager.getExtentReports().createTest(displayName));
        }
    }

    private String formatDuration(long millis) {
        if (millis <= 0) return "0s";
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        long ms = millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis));
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, ms);
    }
}
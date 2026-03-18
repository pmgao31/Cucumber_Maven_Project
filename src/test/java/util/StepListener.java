package util;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import com.aventstack.extentreports.MediaEntityBuilder;

import java.time.Instant;

public class StepListener implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {

        publisher.registerHandlerFor(TestStepStarted.class, event -> {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
                String stepText = step.getStep().getKeyword() + step.getStep().getText();

                ScenarioContext.setCurrentStep(stepText);
                // reset status at start
                ScenarioContext.setCurrentStepStatus(null);
            }
        });

        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                Result result = event.getResult();
                if (result != null) {
                    String status = result.getStatus().name(); // e.g., PASSED, FAILED, SKIPPED
                    ScenarioContext.setCurrentStepStatus(status);

                    // Also log directly to Extent to ensure accurate step-level reporting
                    if (ExtentTestManager.getTest() != null) {
                        String stepText = ScenarioContext.getCurrentStep();
                        try {
                            if ("FAILED".equalsIgnoreCase(status)) {
                                String screenshot = null;
                                try {
                                    screenshot = ScreenshotUtil.takeScreenshot(DriverFactory.getDriver(), "failure_" + sanitize(ScenarioContext.getScenarioName()));
                                } catch (Exception e) {
                                    // ignore screenshot failures
                                }
                                if (screenshot != null) {
                                    ExtentTestManager.getTest().fail("Step failed: " + stepText + " at " + Instant.now().toString(), MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
                                } else {
                                    ExtentTestManager.getTest().fail("Step failed: " + stepText + " at " + Instant.now().toString());
                                }
                            } else if ("SKIPPED".equalsIgnoreCase(status)) {
                                ExtentTestManager.getTest().skip(stepText);
                            } else {
                                ExtentTestManager.getTest().pass(stepText);
                            }
                        } catch (Exception e) {
                            // best-effort logging; do not fail tests because of reporting
                        }
                    }
                }
            }
        });
    }

    private String sanitize(String s) {
        return s == null ? "" : s.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
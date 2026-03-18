package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;

import util.DriverFactory;
import util.ExtentManager;
import util.ExtentTestManager;
import util.ScreenshotUtil;
import util.ScenarioContext;

import java.time.Instant;

public class Hooks {

    private static ExtentReports extent = ExtentManager.getExtentReports();

    @Before
    public void beforeScenario(Scenario scenario) {
        // initialize driver
        DriverFactory.initDriver();
        // set scenario context
        ScenarioContext.setScenarioName(scenario.getName());
        // create extent test for this scenario
        ExtentTestManager.setTest(extent.createTest(scenario.getName()));
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        // Keep step tracking simple and reliable: set a short placeholder. If you want actual Gherkin text,
        // it's better to set it explicitly in the step definitions or use a dedicated cucumber plugin.
        
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        // log step result; if failed, capture screenshot and attach
        if (scenario.isFailed()) {
            String screenshotPath = ScreenshotUtil.takeScreenshot(DriverFactory.getDriver(), "failure_" + sanitize(ScenarioContext.getScenarioName()));
            try {
                ExtentTestManager.getTest().fail("Step failed: " + ScenarioContext.getCurrentStep() + " at " + Instant.now().toString(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                ExtentTestManager.getTest().fail("Step failed and screenshot capture failed: " + e.getMessage());
            }
        } else {
        	
            // mark step as passed in extent
            if (ExtentTestManager.getTest() != null) {
                ExtentTestManager.getTest().pass(ScenarioContext.getCurrentStep());
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        // flush extent (write report)
    
        extent.flush();
        // quit driver
        DriverFactory.quitDriver();
        ExtentTestManager.removeTest();
        ScenarioContext.clearAll();
    }

    private String sanitize(String s) {
        return s == null ? "" : s.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
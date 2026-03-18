package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

import com.aventstack.extentreports.ExtentReports;

import util.DriverFactory;
import util.ExtentManager;
import util.ExtentTestManager;
import util.ScenarioContext;

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
        // placeholder
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        // Step-level logging and screenshots are handled by util.StepListener to
        // ensure accurate per-step status from Cucumber events. Keep this method
        // intentionally empty to avoid duplicate entries in Extent.
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
}
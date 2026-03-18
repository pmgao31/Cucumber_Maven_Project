package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

@Listeners({ util.TestListener.class, util.RetryTransformer.class })
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"stepDefinitions","hooks"},
    plugin = {"pretty","json:target/cucumber-reports/cucumber.json","util.StepListener" },
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
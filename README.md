# Cucumber_BDD

This is a sample Selenium Cucumber BDD project using TestNG, Log4j2, and ExtentReports.

How to run
1. Ensure Java 17 and Maven are installed.
2. From project root, run: mvn test

Configuration
- Edit `src/test/resources/config.properties` to change URL, username, password, or browser.


## Quick test run & reports

Run tests (full):

```cmd
cd "C:\Users\Prasanth M\eclipse-workspace\Cucumber_BDD"
mvn clean test
```

Build without running tests:

```cmd
mvn -DskipTests package
```

Reports and artifacts
- Extent HTML reports: target/extent-reports/ (files created by ExtentManager)
- Cucumber JSON: target/cucumber-reports/cucumber.json
- Screenshots: target/screenshots/ (filenames include scenario and step when captured)

Notes
- Cucumber hooks (`hooks.Hooks`) initialize the WebDriver via `util.DriverFactory.initDriver()` before each scenario and create an Extent test entry with a timestamp.
- TestNG listener (`util.TestListener`) captures screenshots on failure and attaches them to Extent reports. It also uses `util.ScenarioContext` to include scenario name and current step in captions.
- If you see Chrome/CDP warnings in logs, you can optionally add a selenium-devtools dependency matching your browser's CDP channel.

Troubleshooting
- If a test reports NPE about driver being null, ensure `hooks.Hooks` is executing (Cucumber hooks must be on the glue path) and that your step definitions call `DriverFactory.getDriver()`.
- If Extent tests are null in the listener, the listener will create a fallback Extent test; prefer hooks to create the test for richer context.

Next steps
- I can add `.gitignore` entries for the Extent reports and screenshots, or further refine the listener to capture step-level details from Cucumber more precisely.
package stepDefinitions;

import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.cucumberbdd.pageobjects.HomePage;
import com.cucumberbdd.pageobjects.LoginPage;
import util.ConfigReader;
import util.CommonMethods;
import util.DriverFactory;

public class LoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;

    @Given("user navigates to the application")
    public void navigateToApp() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.get("app.url"));
        loginPage = new LoginPage(driver);
    }

    @When("user logs in with valid credentials")
    public void logIn() {
        loginPage.enterUsername(ConfigReader.get("app.username"));
        loginPage.enterPassword(ConfigReader.get("app.password"));
        loginPage.clickLogin();
    }

    @Then("user should see the home page title")
    public void verifyHomePageTitle() {
        homePage = new HomePage(driver);
        String title = homePage.getTitleText();
        CommonMethods.assertTrue(title != null && !title.isEmpty(), "Home page title should be visible");
    }
}
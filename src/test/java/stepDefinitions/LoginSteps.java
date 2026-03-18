package stepDefinitions;

import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.cucumberbdd.pageobjects.HomePage;
import com.cucumberbdd.pageobjects.LoginPage;
import util.ConfigReader;
import util.Constants;
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
        String expectedTitle = Constants.HOME_PAGE_TITLE;
    	homePage = new HomePage(driver);
        String title = homePage.getTitleText();
        CommonMethods.assertEquals(title,expectedTitle, "Home page title should be visible");
    }
    
    @When("user logs in with invalid credentials")
    public void invalidLogIn() {
        loginPage.enterUsername(ConfigReader.get("app.invalidusername"));
        loginPage.enterPassword(ConfigReader.get("app.invalidpassword"));
        loginPage.clickLogin();
    }
    
    
    @Then("user should see an error message")
    public void verifyErrorMessage() {
		String errorMsg = loginPage.getErrorMessage();
		CommonMethods.assertTrue(errorMsg.contains(Constants.INVALID_LOGIN_ERROR), "Error message should indicate invalid credentials");

    }
}
package stepDefinitions;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.cucumber.java.en.*;
import pageObjects.Checkout_page;
import pageObjects.Loginpage;
import pageObjects.SwagLabs_Homepage;

public class Incorrect_Username {
	WebDriver driver;
	Loginpage lg;
	SwagLabs_Homepage home;
	Checkout_page check_out;
	String selected_product = "";
	List<WebElement> products;
	List<WebElement> price;

	@SuppressWarnings("deprecation")
	@Given("Open chrome and launch the Url {string}")
	public void open_chrome_and_launch_the_url(String url) {
		check_out = new Checkout_page(driver);
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Prasanth M\\eclipse-workspace\\Cucumber_Maven_Project\\resources\\drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Given("Enter wrong usernamme as {string} and pwd as {string}")
	public void enter_wrong_usernamme_as_and_pwd_as(String uname, String pwd) {
		lg = new Loginpage(driver);
		lg.enter_username(uname);
		lg.enter_password(pwd);
	}

	@Then("verify the page title {string} is not displayed")
	public void verify_the_page_title_is_not_displayed(String pagetitle) {
		lg = new Loginpage(driver);
		lg.click_login_btn();
		org.testng.Assert.assertEquals(pagetitle, driver.getTitle());
	}

}

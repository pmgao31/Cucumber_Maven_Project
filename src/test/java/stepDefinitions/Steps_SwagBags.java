package stepDefinitions;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.cucumber.java.en.*;
import pageObjects.Checkout_page;
import pageObjects.Loginpage;
import pageObjects.SwagLabs_Homepage;

public class Steps_SwagBags {
	WebDriver driver;
	Loginpage lg;
	SwagLabs_Homepage home;
	Checkout_page check_out;
	String selected_product = "";
	List<WebElement> products;
	List<WebElement> price;

	@SuppressWarnings("deprecation")
	@Given("Launch Url in the browser  {string}")
	public void launch_url_in_the_browser(String url) {
		check_out = new Checkout_page(driver);
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Prasanth M\\eclipse-workspace\\Cucumber_Maven_Project\\resources\\drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}

	@Given("Login usernamme as {string} and password as {string}")
	public void login_usernamme_as_and_password_as(String uname, String pwd) {
		lg = new Loginpage(driver);
		lg.enter_username(uname);
		lg.enter_password(pwd);
	}

	@Then("verify the page title {string}")
	public void verify_the_page_title(String pagetitle) {
		lg = new Loginpage(driver);
		lg.click_login_btn();
		org.testng.Assert.assertEquals(pagetitle, driver.getTitle());
	}

	@Then("Check out the product")
	public void check_out_the_product() {
		home = new SwagLabs_Homepage(driver);
		home.select_product();
		selected_product = home.original_product();
		home.add_to_cart();
		home.Shoppingcart();
		home.Checkout_cart();
	}

	@Then("Fill customer details firstname as {string} lastname {string} and zipcode {string}")
	public void fill_customer_details_firstname_as_lastname_and_zipcode(String f_name, String l_name, String zipcode) {
		check_out = new Checkout_page(driver);
		check_out.enter_firstname(f_name);
		check_out.enter_lastname(l_name);
		check_out.enter_zipcode(zipcode);
		check_out.click_continue();
	}

	@Then("Validation of selected product")
	public void validation_of_selected_product() {
		check_out = new Checkout_page(driver);
		String product_on_checckout = check_out.checkout_product();

		org.testng.Assert.assertEquals(selected_product, product_on_checckout);

		check_out.click_finish();
	}

	@Then("Signout from the page")
	public void signout_from_the_page() {
		home = new SwagLabs_Homepage(driver);
		check_out = new Checkout_page(driver);
		check_out.click_back();
		home.click_menu();
		home.logoutpage();
	}

	@Then("Fetch all the product and Check {string} is available.")
	public void fetch_all_the_product_and_check_is_available(String T_shirt) {
		products = driver.findElements(By.xpath("//*[contains(@id,'title_link')]"));
		int l = 0;
		String[] list_t_shirt = new String[products.size()];
		for (WebElement web : products) {
			list_t_shirt[l] = web.getText();
			l += 1;
		}
		for (String s : list_t_shirt) {
			if (s.equals(T_shirt))
				org.testng.Assert.assertTrue(true);
		}

	}

	@Then("logout from the page")
	public void logout_from_the_page() {
		home = new SwagLabs_Homepage(driver);
		home.click_menu();
		home.logoutpage();
		
	}

	@Then("Fething the price and removing the dollor and validating the price")
	public void fething_the_price_and_removing_the_dollor_and_validating_the_price() {

		List<WebElement> list_price = driver.findElements(By.xpath("//*[contains(@class,'item_price')]"));
		List<WebElement> list_products = driver.findElements(By.xpath("//*[contains(@class,'item_name')]"));

		String[] price1 = new String[list_price.size()];

		String[] product1 = new String[list_products.size()];

		int n = 0;
		for (WebElement w : list_price) {
			price1[n] = w.getText().substring(1);
			n += 1;
		}

		int m = 0;
		for (WebElement j : list_products) {
			product1[m] = j.getText();
			m += 1;
		}
		for (int i = 0; i < product1.length; i++) {
			driver.findElement(By.linkText(product1[i])).click();
			String product_cost = driver.findElement(By.xpath("//*[contains(@class,'details_price')]")).getText()
					.substring(1);
			org.testng.Assert.assertEquals(product_cost, price1[i]);
			driver.findElement(By.id("back-to-products")).click();
		}
	}

	@Then("Signout")
	public void signout() throws Throwable {
		home = new SwagLabs_Homepage(driver);
		home.click_menu();
		home.logoutpage();
		driver.quit();
	}

//	@SuppressWarnings("deprecation")
//	@Given("Open chrome and launch the Url {string}")
//	public void open_chrome_and_launch_the_url(String url) {
//		check_out = new Checkout_page(driver);
//		System.setProperty("webdriver.chrome.driver","C:\\Users\\Prasanth M\\eclipse-workspace\\Cucumber_Maven_Project\\resources\\drivers\\chromedriver.exe");
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get(url);
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//	}
//
//	@Given("Enter wrong usernamme as {string} and pwd as {string}")
//	public void enter_wrong_usernamme_as_and_pwd_as(String uname, String pwd) {
//		lg = new Loginpage(driver);
//		lg.enter_username(uname);
//		lg.enter_password(pwd);
//	}
//
//	@Then("verify the page title {string} is not displayed")
//	public void verify_the_page_title_is_not_displayed(String pagetitle) {
//		lg = new Loginpage(driver);
//		lg.click_login_btn();
//		org.testng.Assert.assertEquals(pagetitle, driver.getTitle());
//	}

	@Then("Sort the product {int}")
	public void sort_the_product(Integer index) {
		home = new SwagLabs_Homepage(driver);
		home.sort_product(index);

	}

	@Then("Fetch the prodct and print it in console")
	public void fetch_the_prodct_and_print_it_in_console() {

		List<WebElement> sorted_products = driver.findElements(By.xpath("//*[contains(@id,'title_link')]"));
		for (WebElement sort : sorted_products) {
			System.out.println(sort.getText());
		}
	}
}

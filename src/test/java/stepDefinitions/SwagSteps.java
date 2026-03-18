package stepDefinitions;

import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import java.util.List;
import org.openqa.selenium.WebElement;
import com.cucumberbdd.pageobjects.CheckoutPage;
import com.cucumberbdd.pageobjects.SwagLabsHomepage;
import util.CommonMethods;
import util.TestDataReader;


public class SwagSteps {
    
    WebDriver driver;
    SwagLabsHomepage home;
    CheckoutPage check_out;
    String selected_product = "";
    List<WebElement> products;
    List<WebElement> price;

    public SwagSteps() {
        // obtain the thread-local driver from the test framework and initialize page objects
        this.driver = CommonMethods.getDriver();
        this.home = new SwagLabsHomepage(this.driver);
        this.check_out = new CheckoutPage(this.driver);
    }

    
    @Then("Check out the product")
    public void checkOutTheProduct() {
        home.select_product();
        selected_product = home.original_product();
        home.add_to_cart();
        home.Shoppingcart();
        home.Checkout_cart();
    }

    @Then("Fill customer details firstname lastname and zipcode")
    public void fillCustomerDetails() {
        
        String f_name= TestDataReader.get("checkout.defaultUser.firstname");
		check_out.enter_firstname(f_name);
        String l_name= TestDataReader.get("checkout.defaultUser.lastname");
		check_out.enter_lastname(l_name);
        String zipcode= TestDataReader.get("checkout.defaultUser.zipcode");
		check_out.enter_zipcode(zipcode);
        check_out.click_continue();
    }

    @Then("Validation of selected product")
    public void validateProduct() {
        String product_on_checckout = check_out.checkout_product();
        CommonMethods.assertEquals(selected_product, product_on_checckout,"Selected product should match the product on checkout page");

        check_out.click_finish();
    }

    @Then("Signout from the page")
    public void SignOut() {
        check_out.click_back();
        home.click_menu();
        home.logoutpage();
    }

    @Then("Fetch all the product and Check certain product is available")
    public void verifyProductAvailable() {
        String T_shirt = TestDataReader.get("products.tshirt");
        products = home.getProductTitleElements();
        int l = 0;
        String[] list_t_shirt = new String[products.size()];
        for (WebElement web : products) {
            list_t_shirt[l] = web.getText();
            l += 1;
        }
        boolean found = false;
        for (String s : list_t_shirt) {
            if (s.equals(T_shirt)) { found = true; break; }
        }
        CommonMethods.assertTrue(found, "T-shirt should be available in the product list");

    }

    @Then("logout from the page")
    public void logout() {
        home.click_menu();
        home.logoutpage();
        
    }

    @Then("Fething the price and removing the dollor and validating the price")
    public void verifyThePrice() {

        List<WebElement> list_price = home.getProductPriceElements();
        List<WebElement> list_products = home.getProductTitleElements();

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
            home.openProductByName(product1[i]);
            String product_cost = home.getCurrentProductDetailsPrice().substring(1);
            CommonMethods.assertEquals(product_cost, price1[i],"Price should match the product details page");
            // use page object helper to click back
            home.clickBackToProducts();
        }
    }

    @Then("Signout")
    public void signout() throws Throwable {
        home.click_menu();
        home.logoutpage();
        // prefer using framework driver reference
//        DriverFactory.quitDriver();
    }

    
    @Then("Sort the product by index")
    public void sortTheProduct() {
    	int index = Integer.parseInt(TestDataReader.get("sorting.index"));
        home.sort_product(index);

    }

    @Then("Fetch the product and print it in console")
    public void printTheProductInConsole() {

        List<WebElement> sorted_products = home.getProductTitleElements();
        for (WebElement sort : sorted_products) {
            System.out.println(sort.getText());
        }
    }

//    
    
}
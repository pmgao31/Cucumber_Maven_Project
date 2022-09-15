package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class SwagLabs_Homepage {

	WebDriver driver;
	String product="";
	
//  $$$$$$ Add to cart elements $$$$$$$$$$$$
	@FindBy(how=How.XPATH ,xpath="//a[@id='item_4_title_link']")
	WebElement select_product;
	
	@FindBy(how=How.ID ,id="add-to-cart-sauce-labs-backpack")
	WebElement add_to_cart;
	
	@FindBy(how=How.XPATH ,xpath="//*[contains(text(),'Backpack')]")
	WebElement product_validation;
	
	@FindBy(how=How.ID ,id="shopping_cart_container")
	WebElement shop_cart;

	@FindBy(how=How.ID ,id="checkout")
	WebElement checkout;
	
//	******Logout elements***********
	@FindBy(how=How.ID ,id="react-burger-menu-btn")
	WebElement menu;
	@FindBy(how=How.ID ,id="logout_sidebar_link")
	WebElement logout;
	
//	******Sorting elements***********

	@FindBy(how=How.TAG_NAME ,tagName="select")
	WebElement sort_ele;
	
	
	public SwagLabs_Homepage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	public void select_product() {
		select_product.click();
	}
	public void add_to_cart() {
		add_to_cart.click();
	}
	public void Shoppingcart() {
		shop_cart.click();
	}
	public void Checkout_cart() {
		checkout.click();
	}
	public String original_product() {
		product=product_validation.getText();
		
		return product;
	}
	public void click_menu() {
		menu.click();
	}
	public void logoutpage() {
		logout.click();
	}
	public void sort_product(int n) {
		new Select(sort_ele).selectByIndex(n);;
	}
	
}

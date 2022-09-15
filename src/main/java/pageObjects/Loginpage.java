package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class Loginpage {
	
	WebDriver driver;
	
	@FindBy(how=How.ID ,id="user-name")
	WebElement txt_uname;
	
	@FindBy(how=How.ID ,id="password")
	WebElement txt_pwd;

	@FindBy(how=How.ID ,id="login-button")
	WebElement login_btn;
	
	public Loginpage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	
	public void enter_username(String u_name) {
		txt_uname.sendKeys(u_name);
		
	}

	public void enter_password(String pwd) {
		txt_pwd.sendKeys(pwd);
		
	}
	public void click_login_btn() {
		login_btn.click();
		
	}
}

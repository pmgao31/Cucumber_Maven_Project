package com.cucumberbdd.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;
import java.util.List;

public class CheckoutPage extends BasePage {

    WebDriver driver;
    String product;
    @FindBy(id="first-name")
    WebElement firstname;
    
    @FindBy(id="last-name")
    WebElement lastname;
    @FindBy(id="postal-code")
    WebElement zipcode;

    @FindBy(id="continue")
    WebElement continue_btn;

    @FindBy(id="finish")
    WebElement finish_btn;
    
    @FindBy(id="back-to-products")
    WebElement back_home_btn;
    
    @FindBy(xpath="//*[contains(text(),'Backpack')]")
    WebElement ck_product;
    
    public CheckoutPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
        PageFactory.initElements(driver,this);
    }
    
    private WebElement resolveFirstName() {
        try { if (firstname != null && firstname.isDisplayed()) return firstname; } catch (Exception ignored) {}
        List<WebElement> els = driver.findElements(By.cssSelector("input[id='first-name'], input[name='firstName'], input[placeholder*='First']"));
        if (!els.isEmpty()) return els.get(0);
        throw new RuntimeException("First name input not found");
    }

    private WebElement resolveLastName() {
        try { if (lastname != null && lastname.isDisplayed()) return lastname; } catch (Exception ignored) {}
        List<WebElement> els = driver.findElements(By.cssSelector("input[id='last-name'], input[name='lastName'], input[placeholder*='Last']"));
        if (!els.isEmpty()) return els.get(0);
        throw new RuntimeException("Last name input not found");
    }

    private WebElement resolvePostalCode() {
        try { if (zipcode != null && zipcode.isDisplayed()) return zipcode; } catch (Exception ignored) {}
        List<WebElement> els = driver.findElements(By.cssSelector("input[id='postal-code'], input[name='postalCode'], input[placeholder*='Zip']"));
        if (!els.isEmpty()) return els.get(0);
        throw new RuntimeException("Postal code input not found");
    }

    private WebElement resolveContinueBtn() {
        try { if (continue_btn != null && continue_btn.isDisplayed()) return continue_btn; } catch (Exception ignored) {}
        List<WebElement> els = driver.findElements(By.xpath("//button[normalize-space(text())='Continue' or normalize-space(text())='CONTINUE']"));
        if (!els.isEmpty()) return els.get(0);
        els = driver.findElements(By.cssSelector("button[id*='continue'], input[type='submit']"));
        if (!els.isEmpty()) return els.get(0);
        throw new RuntimeException("Continue button not found");
    }

    public void enter_firstname(String f_name) {
        WebElement el = resolveFirstName();
        sendKeys(el, f_name);
    }

    public void enter_lastname(String l_name) {
        WebElement el = resolveLastName();
        sendKeys(el, l_name);
    }
    public void enter_zipcode(String zip) {
        WebElement el = resolvePostalCode();
        sendKeys(el, zip);
    }
    public void click_continue() {
        WebElement el = resolveContinueBtn();
        click(el);
    }
    public String checkout_product() {
        return getText(ck_product);
    }
    public void click_finish() {
        click(finish_btn);
    }
    public void click_back() {
        click(back_home_btn);
    }
}
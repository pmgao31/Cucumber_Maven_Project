package com.cucumberbdd.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {

    @FindBy(how = How.ID, id = "user-name")
    WebElement username;

    @FindBy(how = How.ID, id = "password")
    WebElement password;

    @FindBy(how = How.ID, id = "login-button")
    WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String user) {
        sendKeys(username, user);
    }

    public void enterPassword(String pwd) {
        sendKeys(password, pwd);
    }

    public void clickLogin() {
        click(loginBtn);
    }
}

package com.cucumberbdd.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import java.util.List;

import com.cucumberbdd.pageobjects.WaitUtilsProvider;

public class SwagLabsHomepage {

    WebDriver driver;
    String product="";

//  $$$$$$ Add to cart elements $$$$$$$$$$$$
    @FindBy(xpath="//a[@id='item_4_title_link']")
    WebElement select_product;
    
    @FindBy(id="add-to-cart-sauce-labs-backpack")
    WebElement add_to_cart;
    
    @FindBy(xpath="//*[contains(text(),'Backpack')]")
    WebElement product_validation;
    
    // click the actual shopping cart link
    @FindBy(css=".shopping_cart_link")
    WebElement shop_cart;

    @FindBy(id="checkout")
    WebElement checkout;
    
//    ******Logout elements***********
    @FindBy(id="react-burger-menu-btn")
    WebElement menu;
    @FindBy(id="logout_sidebar_link")
    WebElement logout;
//    ******Sorting elements***********

    @FindBy(tagName="select")
    WebElement sort_ele;
    
    
    public SwagLabsHomepage(WebDriver driver) {
        this.driver=driver;
        PageFactory.initElements(driver,this);
    }

    // select first product (robust)
    public void select_product() {
        try {
            List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item_name"));
            if (!items.isEmpty()) {
                WebElement first = items.get(0);
                WaitUtilsProvider.get().waitForClickable(first);
                first.click();
                return;
            }
        } catch (Exception ignored) {}
        // fallback to original PageFactory element
        WaitUtilsProvider.get().waitForClickable(select_product);
        select_product.click();
    }
    
    public void add_to_cart() {
        try {
            // try to find a visible add-to-cart button on the current page
            List<WebElement> buttons = driver.findElements(By.xpath("//button[contains(@id,'add-to-cart') or normalize-space(text())='Add to cart']"));
            for (WebElement b : buttons) {
                try {
                    WaitUtilsProvider.get().waitForClickable(b);
                    b.click();
                    return;
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        // fallback to PageFactory field
        WaitUtilsProvider.get().waitForClickable(add_to_cart);
        add_to_cart.click();
    }
    
    public void Shoppingcart() {
        WaitUtilsProvider.get().waitForClickable(shop_cart);
        shop_cart.click();
    }
    public void Checkout_cart() {
        // try to click a visible Checkout button (cart page markup may vary)
        try {
            List<WebElement> els = driver.findElements(By.xpath("//button[normalize-space(text())='Checkout' or contains(@id,'checkout')] | //a[normalize-space(text())='Checkout']"));
            for (WebElement e : els) {
                try {
                    WaitUtilsProvider.get().waitForClickable(e);
                    e.click();
                    return;
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        // fallback to PageFactory field
        try {
            WaitUtilsProvider.get().waitForClickable(checkout);
            checkout.click();
        } catch (Exception e) {
            // last resort navigate directly if page contains an anchor
            try {
                WebElement fallback = driver.findElement(By.xpath("//a[contains(@href,'checkout')]"));
                WaitUtilsProvider.get().waitForClickable(fallback);
                fallback.click();
            } catch (Exception ex) {
                throw new RuntimeException("Unable to click Checkout", ex);
            }
        }
    }
    public String original_product() {
        try {
            WaitUtilsProvider.get().waitForVisible(product_validation);
            product=product_validation.getText();
            return product;
        } catch (Exception ignored) {}
        // fallback: try to read first product title on listing
        try {
            WebElement title = driver.findElement(By.cssSelector(".inventory_item_name"));
            WaitUtilsProvider.get().waitForVisible(title);
            return title.getText();
        } catch (Exception e) {
            return "";
        }
    }
    public void click_menu() {
        WaitUtilsProvider.get().waitForClickable(menu);
        menu.click();
    }
    public void logoutpage() {
        try {
            List<WebElement> els = driver.findElements(By.id("logout_sidebar_link"));
            if (!els.isEmpty()) {
                WaitUtilsProvider.get().waitForClickable(els.get(0));
                els.get(0).click();
                return;
            }
            // fallback: find by link text
            WebElement link = driver.findElement(By.linkText("Logout"));
            WaitUtilsProvider.get().waitForClickable(link);
            link.click();
        } catch (Exception e) {
            // last resort try clicking the PageFactory-provided element
            try { WaitUtilsProvider.get().waitForClickable(logout); logout.click(); } catch (Exception ignored) {}
        }
    }
    public void sort_product(int n) {
        new Select(sort_ele).selectByIndex(n);;
    }

    // New helper methods to centralize element access and interactions
    public List<WebElement> getProductTitleElements() {
        List<WebElement> elems = driver.findElements(By.xpath("//a[contains(@class,'inventory_item_name') or contains(@id,'title_link')]") );
        // ensure each is visible before returning
        for (WebElement e : elems) {
            try { WaitUtilsProvider.get().waitForVisible(e); } catch (Exception ignored) {}
        }
        return elems;
    }

    public List<WebElement> getProductPriceElements() {
        List<WebElement> elems = driver.findElements(By.xpath("//div[contains(@class,'inventory_item_price') or contains(@class,'item_price')]") );
        for (WebElement e : elems) {
            try { WaitUtilsProvider.get().waitForVisible(e); } catch (Exception ignored) {}
        }
        return elems;
    }

    public void openProductByName(String name) {
        // try matching elements with inventory_item_name text using contains(), case-insensitive
        try {
            List<WebElement> titles = driver.findElements(By.cssSelector(".inventory_item_name"));
            String target = name == null ? "" : name.trim().toLowerCase();
            for (WebElement t : titles) {
                try {
                    String txt = t.getText();
                    if (txt != null && txt.trim().toLowerCase().contains(target)) {
                        WaitUtilsProvider.get().waitForClickable(t);
                        t.click();
                        return;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        // try link text and other fallbacks
        try {
            List<WebElement> links = driver.findElements(By.xpath("//a[normalize-space(text())='"+name+"']"));
            if (!links.isEmpty()) {
                WaitUtilsProvider.get().waitForClickable(links.get(0));
                links.get(0).click();
                return;
            }
        } catch (Exception ignored) {}
        try {
            WebElement link = driver.findElement(By.xpath("//div[contains(@class,'inventory_item_name') and contains(normalize-space(text()),'"+name+"')]") );
            WaitUtilsProvider.get().waitForClickable(link);
            link.click();
            return;
        } catch (Exception ignored) {}
        // last fallback: try partial link text (case-insensitive not available here)
        try {
            WebElement link = driver.findElement(By.partialLinkText(name));
            WaitUtilsProvider.get().waitForClickable(link);
            link.click();
        } catch (Exception e) {
            throw new RuntimeException("Unable to open product by name: "+name, e);
        }
    }

    public String getCurrentProductDetailsPrice() {
        try {
            List<WebElement> els = driver.findElements(By.xpath("//div[contains(@class,'inventory_details_price') or contains(@class,'details_price') or contains(@class,'inventory_item_price')]") );
            if (!els.isEmpty()) {
                WebElement detailsPrice = els.get(0);
                WaitUtilsProvider.get().waitForVisible(detailsPrice);
                String text = detailsPrice.getText();
                if (text != null && text.length() > 0) {
                    return text;
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public void clickBackToProducts() {
        try {
            WebElement back = driver.findElement(By.id("back-to-products"));
            WaitUtilsProvider.get().waitForClickable(back);
            back.click();
            return;
        } catch (Exception ignored) {}
        // fallback: navigate back
        driver.navigate().back();
    }

}
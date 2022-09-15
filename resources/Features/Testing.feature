
Feature: Swag Bags page functionality
  
 Background: Browser Initializing and logging into the website
    Given Launch Url in the browser  "https://www.saucedemo.com/" 
    And Login usernamme as "standard_user" and password as "secret_sauce"
    Then verify the page title "Swag Labs"
   
  @SanityTest
  Scenario: Checkout validation scenario
    And Check out the product
    Then Fill customer details firstname as "Raja" lastname "Rajan" and zipcode "636704"
    Then Validation of selected product
    And Signout from the page 
    
	@SmokeTest
  Scenario: T_Shirt Validation
    Then Fetch all the product and Check "Sauce Labs Bolt T-Shirt" is available.
    And logout from the page
 
 @RegressionTest
  Scenario: Price validation
    Then Fething the price and removing the dollor and validating the price
    Then Signout
  @End2end
  Scenario: Sorting the product Scenario
    Then Sort the product 2
    Then Fetch the prodct and print it in console
    Then Signout
    
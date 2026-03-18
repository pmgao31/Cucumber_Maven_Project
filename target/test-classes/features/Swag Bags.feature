Feature: Swag Bags page functionality

  Background: Browser Initializing and logging into the website
    Given user navigates to the application
    When user logs in with valid credentials

  @SanityTest
  Scenario: Checkout validation scenario
    And Check out the product
    Then Fill customer details firstname lastname and zipcode
    Then Validation of selected product
    And Signout from the page

  @SmokeTest
  Scenario: T_Shirt Validation
    Then Fetch all the product and Check certain product is available
    And logout from the page

  @RegressionTest
  Scenario: Price validation
    Then Fething the price and removing the dollor and validating the price
    Then Signout

  @End2end
  Scenario: Sorting the product Scenario
    Then Sort the product by index
    Then Fetch the product and print it in console
    Then Signout

Feature: Login to SauceDemo

  Scenario: Successful login with valid credentials
    Given user navigates to the application
    When user logs in with valid credentials
    Then user should see the home page title

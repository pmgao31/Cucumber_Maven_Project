Feature: Login to SauceDemo Application

  Background:
    Given user navigates to the application

  Scenario: Successful login with valid credentials
    When user logs in with valid credentials
    Then user should see the home page title

  Scenario: Login fails with invalid credentials
    When user logs in with invalid credentials
    Then user should see an error message
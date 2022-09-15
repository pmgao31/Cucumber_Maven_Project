
@tag
Feature: Wrong Username feature
  

  @tag1
  Scenario: Invalid User Id
    Given Open chrome and launch the Url "https://www.saucedemo.com/"
    And Enter wrong usernamme as "wrongname" and pwd as "wrong_password"
    Then verify the page title "Swag Labs" is not displayed

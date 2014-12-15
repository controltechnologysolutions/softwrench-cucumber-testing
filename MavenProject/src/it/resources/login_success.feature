Feature: Login sucess
  As a user,
  I want to be able to login to softWrench..
 
  Scenario: Loggin in is successful
    
    Given I am on the login page of softWrench
    When I enter my correct username and password
    Then I should get loggedon to softWrench
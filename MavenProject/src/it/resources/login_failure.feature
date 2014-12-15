Feature: Login sucess
  As a user,
  if I enter a wrong password or username, 
  softWrench should reject my login attempt.
 
  Scenario: Loggin in is successful
    
    Given I am on the login page of softWrench app
    When I enter an incorrect username or password
    Then I should get a respective error message.
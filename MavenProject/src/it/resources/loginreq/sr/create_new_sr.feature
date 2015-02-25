@sr @create @medium 
Feature: I can create a new SR given all required fields are filled
  As a user,
  I want to be able to create a new record.
  If not all required fields are filled, I want to see an error message.
 
  Background: I am logged in
 
  Scenario Outline: Create new SR
    Given I click on the new SR button
    When I fill in the following fields '<fields>' with '<values>' and submit
    Then I should see a '<success_error>' message
    
  Examples:
  	| fields	| values | success_error |
    | description   | Cucumber Test | success |
   
    

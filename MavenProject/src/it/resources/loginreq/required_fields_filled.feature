@sr @details
Feature: Required fields are filled
  When I click on a sr record that was previously created, all required fields are filled.
 
  Scenario Outline: Required fields are filled
    Given I am on the service request grid
    When I click on row <rownumber> in the grid
    Then I should see that all fields that have an asterics are filled.
    
  Examples:
  	| rownumber |
    | 1         |
    | 4         |
    | 10        |
  
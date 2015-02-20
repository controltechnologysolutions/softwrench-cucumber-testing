@grids @sr @inprg
Feature: Details are shown
  As a user,
  I want to be able to click on a row in the service request grid,
  to see the details of a service request.
 
  Scenario Outline: See SR details
    Given I am on the service request grid
    When I click on row <rownumber> in the grid
    Then I should see the details for the service SR I clicked on
    
  Examples:
  	| rownumber |
    | 1         |
  	| 3         |
 #   | 4         |
 #   | 10        |
 #   | 25        | 
  
   
    
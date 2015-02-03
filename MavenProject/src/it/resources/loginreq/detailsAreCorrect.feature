@sr @srdetails
Feature: Clicking on a row brings up correct record
  As a user,
  I want to be able to click on a row in the service request grid,
  and then get directed to the correct record.
 
  Scenario Outline: Correct SR details are shown
    Given I am on the service request grid
    When I click on row <rownumber> in the grid
    Then I should see the record for the service SR I clicked on
    
  Examples:
  	| rownumber |
    | 1         |
    | 4         |
    | 10        |
  
   
    
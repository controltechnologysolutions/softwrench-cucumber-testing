@sr @medium @inprg
Feature: Clicking on the up button in an SR records brings up the previous record. 
Clicking on the down buttom brings up the next record in the grid.
 
  Scenario Outline: Up and down buttons work correctly
    Given I am on the service request grid
    And I remember the order of records in the grid
    When I click on row <row> in the grid
    Then the '<updown>' button should be '<endisabled>'
    Then I click on the '<downup>' button
    Then I should see the '<nextprevious>' record 
    
  Examples:
  	| row	 | updown  | endisabled | downup | nextprevious |
    | 1      | up      | disabled   | down   | previous			|
 #   | 4      | up	   | enabled    | up     | next     |
 #   | last   | down    | disabled   | up     | next     |
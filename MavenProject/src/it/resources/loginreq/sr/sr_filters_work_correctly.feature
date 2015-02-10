@sr @medium
Feature: When I enter a search string in any of the filters for columns,
	the table contents is filtered according to the chosen filter.
 
  Background: I am logged in
 
  Scenario Outline: Filter SR Grid
   Given I am on the service request grid
    When I enter '<filterstring>' into column '<nr>'
    Then I should see only records in the grid that contain '<filterstring>' in column '<nr>'
     And the number of records shown equals the number shown for 'Totel Items'
    
  Examples:
  	| filterstring | nr |
  	| 87		   | 1  |
  	| test 		   | 2  |
    | aman         | 3  |
 	| closed  	   | 4  |
	| robert       | 5  |
    
  
   
    

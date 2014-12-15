Feature: Details are shown
  As a user,
  I want to be able to click on a row in the service request grid,
  to see the details of a service request.
 
  Scenario: See SR details
    
    Given I am on the service request grid
    When I click on a row in the grid
    Then I should see the details for the service SR I clicked on
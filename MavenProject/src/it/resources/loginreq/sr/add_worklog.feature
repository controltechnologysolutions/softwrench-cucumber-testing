 @sr @create @worklog @medium @inprg
Feature: I can create a new worklog given all required fields are filled
  As a user,
  I want to be able to create a new worklog for an SR.
  If not all required fields are filled, I want to see an error message.
 
  Background: I am logged in
 
  Scenario Outline: Create new worklog for SR
    Given I am on the service request grid
    And I click on the <rownumber> record that is not closed in the grid
    And I click on the worklog tab
    When I click on the SR worklog button
    And I fill in the '<fields>'
    Then I should see a '<success_error>' message
    
 Examples:
  	| rownumber | fields               | success_error |
    | 1         | summary, description | success |
    | 4         | summary              | success |
    | 10        | description          | error   |
    
 
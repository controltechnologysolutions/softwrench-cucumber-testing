@sr @create @medium
Feature: Upload an attachment to SR
  As a user,
  I want to be able to upload an attachment to service requests.
 
  Scenario Outline: Upload attachment to SR
    Given I am on the service request grid
    And I click on the <rownumber> record that is not closed in the grid
    And I click on the 'attachment' tab
    When I click on the add attachment button
    And I add an attachment '<attachment>'
    And I fill in the '<fields>' and submit
    Then I should see a '<success_error>' message
    
  Examples:
  	| rownumber | attachment | fields                         | success_error |
    | 3         | yes        | document                       | success       |
    | 3         | no         | document                       | error        |
    | 6         | yes        | document,docinfo_.description  | success       |
    | 6         | no         | document,docinfo_.description  | error       |
    | 15        | yes        | docinfo_.description           | error         |
    
  
    
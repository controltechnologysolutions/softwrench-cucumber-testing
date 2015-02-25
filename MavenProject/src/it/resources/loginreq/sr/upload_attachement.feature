@sr @create @medium @inprg
Feature: Upload an attachment to SR
  As a user,
  I want to be able to upload an attachment to service requests.
 
  Scenario Outline: Upload attachment to SR
    Given I am on the service request grid
    And I click on the <rownumber> record that is not closed in the grid
    And I click on the attachment tab
    When I click on the add attachment button
    And I add an attachment '<attachment>'
    And I fill in the '<fields>' with labels '<field_labels>' and submit
    Then I should see a '<success_error>' message
    
  Examples:
  	| rownumber | attachment | fields                         | field_labels | success_error |
    | 3         | yes        | document                       | Title        | success       |
    | 3         | no         | document                       | Title        | error        |
    | 6         | yes        | document,docinfo_.description  | Title,Description | success       |
    | 6         | no         | document,docinfo_.description  | Title,Description |  error       |
    | 15        | yes        | docinfo_.description           | Description  | error         |
    
  
    
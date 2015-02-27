@sr @create @communication @medium @inprg
Feature: I can create a new communication given all required fields are filled
  As a user,
  I want to be able to create a new communication for an SR.
  If not all required fields are filled, I want to see an error message.
 
  Background: I am logged in
 
  Scenario Outline: Create new worklog for SR
    Given I am on the service request grid
    And I click on the <rownumber> record that is not closed in the grid
    And I click on the 'communication' tab
    When I click on the SR communication button
    And I fill in the following new item fields '<fields>' with '<values>' and submit
    Then I should see a '<success_error>' message
    
 Examples:
  	| rownumber | fields               | values | success_error |
    | 3         | sendto,cc,sendfrom,subject,message | jenkins@controltechnologysolutions.com,jdamerow@controltechnologysolutions.com,jenkins@controltechnologysolutions.com,Jenkins sent you an email,And this is the message | success |
  #  | 6         | description              | success |
  #  | 15        | longdescription_.ldtext           | error   |
    
 
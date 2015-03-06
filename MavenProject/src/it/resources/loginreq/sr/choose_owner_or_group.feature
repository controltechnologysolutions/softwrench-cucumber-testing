@sr @srdetails @medium
Feature: Owner and Owner Group popup 
  When I click on the button to open the owner popup, a popup opens that allows me to choose a user. 
  	   Same is true for owner group popup. If I select either of those two fields and fill it using the
  	   popup, then the other field gets disabled.
 
  Scenario Outline: Owner and Owner Group popup 
    Given I click on the new SR button
    When I click on the '<button>' button
    Then I filter the list in column '<column>' with '<filterstring>'
    Then I click on the <nr> result
    Then I should see a warning that the '<alertMsgField>'
    And I should see that the '<fields>' fields are filled
    And the field '<otherfield>' is disabled.
    
  Examples:
  	| button      | column                 | filterstring | nr | fields              | otherfield | alertMsgField |
    | owner       | lookupObj.code         | red          | 1  | owner   | ownergroup | Owner Group Field will be disabled if the Owner is selected.  |
    | ownergroup  | lookupObj.description  | eng          | 1  | ownergroup  | owner      | Owner Field will be disabled if the Owner Group is selected.  |
 
  
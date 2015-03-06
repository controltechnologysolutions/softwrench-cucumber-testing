@nick
Feature: Profile page
  As a user,
  I want to be able to reach the profile page
 
  Scenario: Profile page appears
    
    Given I am logged in
    When I click on Profile
    Then I am brought to the profile page
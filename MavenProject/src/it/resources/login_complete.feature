@basics
Feature: Login
In order to secure softWrench,
I want to implement login functionality

Background: Given I am on the login page of softWrench

Scenario Outline: Check user's login credentials
Given I filled '<username>' and '<password>'
When I clicked on Login
Then I should see '<success or failure>' message
 
Examples: Valid login
 |username    |password |success or failure  |
 |swadmin     |sw@dm1n  |success             |
 
Examples: InValid login
 |username    |password         |success or failure |
 |xxxx        |xxxx             |failure            |
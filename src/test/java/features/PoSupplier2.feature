      
    #In order to execute this test, PO number and Customer names should be changed for every new execution

@Smoke
Feature: To validate the PO Financing workflow using Supplier Portal

  Background: Supplier creates the funding request and Funder uploads the funding agreement
    Given Supplier navigates to the supplier portal with "<emailAddress>" and "<password>"
    When Supplier completes the transactions with "<companyName>" and uploads the document
    And Supplier creates the PO funding request to the external funder
    And Now Funder navigates to the funder portal using valid credentials
    And Funder makes an offer to the created funding request
    And Supplier views and accepts the funding offer

  Scenario Outline: 
    Given User should validate the PO workflow with manual signature
    When Funder uploads the funding agreement using manual signature
    And Supplier signs the funding agreement
    Then Funder makes the transactions as Mark as Paid

    Examples: 
      | emailAddress              | password | companyName           |
      | cit.test03@mailinator.com | Test@123 | A58 Entertainment Inc |

  Scenario Outline: 
    Given User should validate the PO workflow with E signature
    When Funder uploads the funding agreement using E signature
    And Supplier signs the funding agreement with OTP
    Then Funder makes the transactions as Mark as Paid

    Examples: 
      | emailAddress              | password | companyName           |
      | cit.test04@mailinator.com | Test@123 | A58 Entertainment Inc |

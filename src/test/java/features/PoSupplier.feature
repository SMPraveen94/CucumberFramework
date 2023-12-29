      
    #In order to execute this test, PO number and Customer names should be changed for every new execution


Feature: To validate the PO Financing workflow using Supplier Portal

  @PoSupplier
  Scenario: User validates the PO financing workflow with Manual Signature
    Given Supplier navigates to the supplier portal using valid credentials
    When Supplier completes the transactions and uploads the document
    And Supplier creates the PO funding request to the external funder
    And Now Funder navigates to the funder portal using valid credentials
    And Funder makes an offer to the created funding request
    And Supplier views and accepts the funding offer
    When Funder uploads the funding agreement using manual signature
    And Supplier signs the funding agreement
    Then Funder makes the transactions as Mark as Paid

  Scenario: User validates the PO financing workflow with E Signature 
    Given Supplier navigates the supplier portal with valid credentials
    When Supplier completes transactions and uploads the document
    And Supplier creates PO funding request to external funder
    And Funder navigates to funder portal with valid credentials
    And Funder makes an offer to created funding request
    And Supplier views and accepts funding offer
    When Funder uploads the funding agreement using E signature
    And Supplier signs the funding agreement with OTP
    Then Funder marks the transactions as Mark as Paid


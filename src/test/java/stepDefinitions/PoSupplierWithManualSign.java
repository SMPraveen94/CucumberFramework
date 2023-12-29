package stepDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;

import applicationController.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pojo.SupplierPOTestData;

public class PoSupplierWithManualSign extends BaseTest {

	private String JsonFilePath = config.getProperty("testData");
	private SupplierPOTestData data = new SupplierPOTestData(JsonFilePath);
	public String firstSupplierEmail = data.getSupplierEmailID();
	public String funderMailAddress = data.getFunderEmailAddress();
	private String PoNumber = data.getPO_Number();
	private String companyName = data.getCustomerName();
	private String password = data.getPassword();
	private String PO = config.getProperty("PO");
	private String fundingRequestType = data.getFundingRequestType();
	private String fundingAgreement = "D:\\LF_docs\\Balance_Statement.pdf";
	public String FUNDING_REQUEST_ID;
	public String fundingContractTextID;

	@Given("Supplier navigates to the supplier portal using valid credentials")
	public void supplierNavigatesToTheSupplierPortalUsingValidCredentials() {

		try {
			url(config.getProperty("supplierURL"));
			log.info("Supplier Url is Launched !!!");
			waitForElement(3);
			sendKeys("Email", firstSupplierEmail);
			sendKeys("Password", password);
			click("LoginButton");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Supplier completes the transactions and uploads the document")
	public void supplierCompletesTheTransactionsAndUploadsTheDocument() {

		try {
			jsClick("transactions");
			jsClick("PO_Financing");
			// waitForElement(4);
			jsClick("UploadButton");
			waitForClickabilityOf("PO_FinancingUpload");
			click("PO_FinancingUpload");
			uploadFile(PO);
			String customerNameTitle = or.getProperty("CustomerNameTitle");
			waitForVisibilityByXpath(customerNameTitle);
			String PO_Locator = or.getProperty("PO_Number");
			fluentWaitByXpath(PO_Locator);
			WebElement PO_LocatorTextBox = driver.findElement(By.xpath(PO_Locator));
			PO_LocatorTextBox.clear();
			PO_LocatorTextBox.sendKeys(PoNumber);
			log.info(PoNumber + " is passed to the PO Number TextBox");
			WebElement customerNameLocator = driver.findElement(By.xpath(or.getProperty("CustomerNameTextBox")));
			customerNameLocator.clear();
			customerNameLocator.sendKeys(companyName);
			log.info("Purchase Order Number is " + PoNumber);
			click("PO_UploadButton");
			// waitForElement(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Supplier creates the PO funding request to the external funder")
	public void supplierCreatesThePoFundingRequestToTheExternalFunder() {

		try {
			String PO_locator = "//*[text()='Submitted']/parent::td/preceding-sibling::td[3]/span[text()='" + PoNumber
					+ "']/parent::td/preceding-sibling::td[2]";
			// waitForClickabilityByXpath(PO_locator);
			// waitForElement(10);
			fluentWaitForLocator(PO_locator, 10);
			clickLocator(PO_locator);
			click("CreateFundingRequest");
			if (fundingRequestType.equalsIgnoreCase("NewRequest")) {
				click("CreateNewFundingRequestRadioButton");
				click("CreateFundingRequestPopup");
				String fundRequestNumberLocator = "//*[text()='" + PoNumber + "']/parent::td/parent::tr/td[7]//a";
				fluentWaitByXpath(fundRequestNumberLocator);
				FUNDING_REQUEST_ID = driver.findElement(By.xpath(fundRequestNumberLocator)).getText();
				log.info("Funding Request ID is " + FUNDING_REQUEST_ID);
				clickLocator(fundRequestNumberLocator);
				String FundRequestIdLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']";
				fluentWaitByXpath(FundRequestIdLocator);
				refresh();
				waitForElement(2);
				String moreActionsLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/following::td[5]";
				waitForVisibilityByXpath(moreActionsLocator);
				clickLocator(moreActionsLocator);
				waitForElement(2);
				click("ViewMatchedFunders");
				String funderLocator = "//span[text()='" + config.getProperty("funderName") + "']"
						+ "//parent::td//preceding-sibling::td//input[@type='checkbox']";
				clickLocator(funderLocator);
				click("SendRequestToFunders");
				click("ConfirmAndSubmit");
				waitForElement(5);
//				String requestStatusMessageLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']//following::td[4]//div";
//				String requestStatusMessage = driver.findElement(By.xpath(requestStatusMessageLocator)).getText();
//				Assert.assertEquals(requestStatusMessage, "Awaiting Funding Offer");
			} else {
				click("UseExistingContractRadioButton");
				click("SelectExistingContractDropDown");
				click("ExistingContractList");
				click("CreateFundingRequestPopup");
				String poNumber = "//*text()='"+PoNumber+"']";
				waitForClickabilityByXpath(poNumber);
				String fundRequestNumberLocator = "//*[text()='" + PoNumber + "']/parent::td/parent::tr/td[7]//a";
				//waitForClickabilityByXpath(fundRequestNumberLocator);
				FUNDING_REQUEST_ID = driver.findElement(By.xpath(fundRequestNumberLocator)).getText();
				log.info("Funding Request ID is " + FUNDING_REQUEST_ID);
				clickLocator(fundRequestNumberLocator);
				String FR_ID = "//*[text()='"+FUNDING_REQUEST_ID+"']";
				waitForVisibilityByXpath(FR_ID);
//				String statusLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/following::td[4]/div";
//				String status = driver.findElement(By.xpath(statusLocator)).getText();
//				Assert.assertEquals(status, "Awaiting Funding Offer");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Now Funder navigates to the funder portal using valid credentials")
	public void nowFunderNavigatesToTheFunderPortalUsingValidCredentials() {

		try {
			driver.switchTo().newWindow(WindowType.WINDOW);
			waitForElement(1);
			url(config.getProperty("funderURL"));
			log.info("Funder Url is Launched !!!");
			waitForElement(3);
			sendKeys("Email", funderMailAddress);
			sendKeys("Password", password);
			click("LoginButton");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Funder makes an offer to the created funding request")
	public void funderMakesAnOfferToTheCreatedFundingRequest() {

		try {
			click("NotificationIcon");
			String NotificationMessageLocator = "//*[contains(text(),'"+FUNDING_REQUEST_ID+"')]//preceding::span[1]";
			fluentWaitByXpath(NotificationMessageLocator);
			clickLocator(NotificationMessageLocator);
			waitForElement(3);
			String actionsLocator = "//*[text()='"+FUNDING_REQUEST_ID+"']/following::div[6]";
			waitForVisibilityByXpath(actionsLocator);
			clickLocator(actionsLocator);
			String makeAnOfferLocator = "//a[text()='" + FUNDING_REQUEST_ID
					                  + "']/parent::div/parent::div/following::div[5]//"
					                  + "button[@type='button' and normalize-space()='Actions']"
					                  + "/following::div[1]//button[text()='Make Offer']";
			clickLocator(makeAnOfferLocator);
			if (fundingRequestType.equalsIgnoreCase("NewRequest")) {
				click("AdvanceRate");
				sendKeys("AdvanceRate", "20");
				click("FactoringRate");
				sendKeys("FactoringRate", "0.12");
				sendKeys("OtherFee", "Processing fee - 5%");
				click("MakeAnOffer");
				waitForInvisibilityOf("MakeAnOffer");
			}else {
				click("ApproveRequest");
			}	
			switchToWindow(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Supplier views and accepts the funding offer")
	public void supplierViewsAndAcceptsTheFundingOffer() {

		try {
			waitForElement(5);
			refresh();
			click("NotificationIcon");
			String notificationLocator = "(//*[contains(text(), '" + FUNDING_REQUEST_ID + "')]//preceding::p//span)[1]";
			waitForElement(5);
			// fluentWaitForLocator(notificationLocator, 5);
			clickLocator(notificationLocator);
			String dotsOptionLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::td/following::Td[5]/button";
			waitForElement(3);
			// fluentWaitForLocator(dotsOptionLocator, 3);
			clickLocator(dotsOptionLocator);
			click("ViewFundingOffer");
			click("FunderRadioButton");
			click("AcceptFundingOffer");
			waitForVisibilityOf("SupplierAcceptedFundingOffer");
			switchToWindow(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Funder uploads the funding agreement")
	public void funderUploadsTheFundingAgreement() {

		try {
			waitForElement(3);
			click("NotificationIcon");
			String notificationMessageLocator = "(//*[contains(text(),'" + FUNDING_REQUEST_ID
					+ "')]//preceding::p//span)[1]";
			clickLocator(notificationMessageLocator);
			click("InFundingTab");
			String actionsButtonLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div/parent::div"
					+ "/following-sibling::div[5]//button[normalize-space()='Actions']";
			clickLocator(actionsButtonLocator);
			String uploadFundingAgreement = "//a[text()='" + FUNDING_REQUEST_ID + "']/following::div[6]/div"
					+ "/div/button[2][text()='Upload Funding Agreement']";
			clickLocator(uploadFundingAgreement);
			click("UseManualSignature");
			click("SignatureNextButton");
			waitForElement(2);
			// fluentWaitForLocator(or.getProperty("FundingUpload"), 2);
			click("FundingUpload");
			waitForElement(2);
			// fluentWaitForLocator(or.getProperty("fundingAgreement"), 2);
			uploadFile(fundingAgreement);
			waitForElement(2);
			// fluentWaitForLocator(or.getProperty("FundingUploadButton"), 2);
			click("FundingUploadButton");
			String activeContractLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div"
					+ "/parent::div/following-sibling::div[4]//a";
			WebElement activeContract = driver.findElement(By.xpath(activeContractLocator));
			fundingContractTextID = activeContract.getText();
			log.info("Active Contract ID : " + fundingContractTextID);
			switchToWindow(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Supplier signs the funding agreement")
	public void supplierSignsTheFundingAgreement() {

		try {
			waitForElement(3);
			// fluentWaitByXpath(or.getProperty("NotificationIcon"));
			click("NotificationIcon");
			String notificationMessage = "(//*[contains(text(),'" + FUNDING_REQUEST_ID + "')]//preceding::p//span)[1]";
			clickLocator(notificationMessage);
			String actionsOptionLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/following::td[5]/button";
			clickLocator(actionsOptionLocator);
			click("ViewAndSignAgreement");
			click("SignedAgreementUpload");
			waitForElement(2);
			uploadFile(fundingAgreement);
			waitForElement(2);
			fluentWaitByXpath(or.getProperty("SignedAgreementUploadButton"));
			click("SignedAgreementUploadButton");
			waitForElement(2);
			String messageLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::td/following-sibling::td[4]/div";
			waitForVisibilityByXpath(messageLocator);
			String message = driver.findElement(By.xpath(messageLocator)).getText();
			// Assert.assertEquals(message, "Supplier Accepted Funding Offer");
			click("Logout");
			// fluentWaitByXpath(or.getProperty("Email"));
			switchToWindow(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Funder makes the transactions as Mark as Paid")
	public void funderMakesTheTransactionsAsMarkAsAaid() {

		try {
			waitForElement(3);
			// fluentWaitForLocator(or.getProperty("NotificationIcon"), 3);
			click("NotificationIcon");
			String notificationMessageLocator = "(//*[contains(text(),'" + FUNDING_REQUEST_ID
					+ "')]//preceding::p//span)[1]";
			clickLocator(notificationMessageLocator);
			click("InFundingTab");
			String actionsButtonLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div/parent::"
					+ "div/following-sibling::div[5]//button[normalize-space()='Actions']";
			clickLocator(actionsButtonLocator);
			String markAsPaidByCustomer = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div/parent::div/"
					+ "following-sibling::div[5]//button[normalize-space()='Actions']/"
					+ "following::div[1]/button[2][text()='Mark as \"Paid by customer\"']";
			clickLocator(markAsPaidByCustomer);
			click("MarkAsPaid");
			click("Archived");
			String statusMessageLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div/parent::"
					+ "div/following-sibling::div[3]/button/span";
			String statusMessage = driver.findElement(By.xpath(statusMessageLocator)).getText();
			Assert.assertEquals(statusMessage, "Paid Back");
			// fluentWaitForLocator(or.getProperty("Logout"), 3);
			waitForElement(3);
			click("Logout");
			waitForVisibilityOf("Email");
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Funder uploads the funding agreement using manual signature")
	public void funderUploadsTheFundingAgreementUsingManualSignature() {

		try {
			waitForElement(3);
			click("NotificationIcon");
			String notificationMessageLocator = "(//*[contains(text(),'" + FUNDING_REQUEST_ID
					+ "')]//preceding::p//span)[1]";
			clickLocator(notificationMessageLocator);
			click("InFundingTab");
			String actionsButtonLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div/parent::div"
					+ "/following-sibling::div[5]//button[normalize-space()='Actions']";
			clickLocator(actionsButtonLocator);
			String uploadFundingAgreement = "//a[text()='" + FUNDING_REQUEST_ID + "']/following::div[6]/div"
					+ "/div/button[2][text()='Upload Funding Agreement']";
			clickLocator(uploadFundingAgreement);
			click("UseManualSignature");
			click("SignatureNextButton");
			waitForElement(2);
			// fluentWaitForLocator(or.getProperty("FundingUpload"), 2);
			click("FundingUpload");
			waitForElement(2);
			// fluentWaitForLocator(or.getProperty("fundingAgreement"), 2);
			uploadFile(fundingAgreement);
			waitForElement(2);
			// fluentWaitForLocator(or.getProperty("FundingUploadButton"), 2);
			click("FundingUploadButton");
			String activeContractLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']/parent::div"
					+ "/parent::div/following-sibling::div[4]//a";
			WebElement activeContract = driver.findElement(By.xpath(activeContractLocator));
			fundingContractTextID = activeContract.getText();
			log.info("Active Contract ID : " + fundingContractTextID);
			switchToWindow(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package stepDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import applicationController.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pojo.SupplierPOTestData;

public class PoSupplierWithESign extends BaseTest {

	private String JsonFilePath = config.getProperty("testData");
	private SupplierPOTestData data = new SupplierPOTestData(JsonFilePath);
	private String supplierEmailAddress = data.getSecondSupplierData();
	private String supplierPassword = data.getPassword();
	private String companyName = data.getCustomerName();
	private String funderEmailAddress = data.getFunderEmailAddress();
	private String PoNumber = data.getPO_Number();
	private String fundingAgreement = "./src/test/resources/files/Balance_Statement.pdf";
	private String PO = "D:\\Repo\\FSC-\\src\\test\\resources\\files\\PO.pdf";
	private String FUNDING_REQUEST_ID;
	private String OTP;
	private String fundingContractTextID;
	
	@Given("Supplier navigates the supplier portal with valid credentials")
	public void supplierNavigatesTheSupplierPortalWithValidCredentials() {
	   
		try {
		    url("http://localhost:3000/supplier");
			waitForElement(3);
			log.info("Supplier Url is Launched !!!");
			sendKeys("Email", supplierEmailAddress);
			sendKeys("Password", supplierPassword);
			click("LoginButton");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@When("Supplier completes transactions and uploads the document")
	public void supplierCompletesTransactionsAndUploadsTheDocument() {

		try {
			jsClick("transactions");
			jsClick("PO_Financing");
			waitForElement(4);
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
			waitForElement(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("Supplier creates PO funding request to external funder")
	public void supplierCreatesPOFundingRequestToExternalFunder() {
	 
		try {
			String PO_locator = "//*[text()='Submitted']/parent::td/preceding-sibling::td[3]/span[text()='" + PoNumber
					          + "']/parent::td/preceding-sibling::td[2]";
			// waitForClickabilityByXpath(PO_locator);
			waitForElement(10);
			clickLocator(PO_locator);
			click("CreateFundingRequest");
			click("CreateFundingRequestPopup");
			waitForElement(5);
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
			String requestStatusMessageLocator = "//*[text()='" + FUNDING_REQUEST_ID + "']//following::td[4]//div";
			String requestStatusMessage = driver.findElement(By.xpath(requestStatusMessageLocator)).getText();
			Assert.assertEquals(requestStatusMessage, "Awaiting Funding Offer");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@When("Funder navigates to funder portal with valid credentials")
	public void funderNavigatesToFunderPortalWithValidCredentials() {
	   
		try {
			driver.switchTo().newWindow(WindowType.WINDOW);
			waitForElement(1);
			url("http://localhost:3000/funder");
			log.info("Funder Url is Launched !!!");
			waitForElement(3);
			sendKeys("Email", "cit.testfunder01@mailinator.com");
			sendKeys("Password", "Test@123");
			click("LoginButton");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("Funder makes an offer to created funding request")
	public void funderMakesAnOfferToCreatedFundingRequest() {
	   
		try {

			click("NotificationIcon");
			String NotificationMessageLocator = "(//*[contains(text(),'" + FUNDING_REQUEST_ID
					                          + "')]//preceding::p//span)[1]";
			fluentWaitByXpath(NotificationMessageLocator);
			clickLocator(NotificationMessageLocator);
			Thread.sleep(3000);
			String actionsLocator = "//a[text()='" + FUNDING_REQUEST_ID + "']/parent::div/parent::div/following::"
					              + "div[5]//button[@type='button' and normalize-space()='Actions']";
			waitForVisibilityByXpath(actionsLocator);
			clickLocator(actionsLocator);
			String makeAnOfferLocator = "//a[text()='" + FUNDING_REQUEST_ID
					                  + "']/parent::div/parent::div/following::div[5]//"
					                  + "button[@type='button' and normalize-space()='Actions']"
				                      + "/following::div[1]//button[text()='Make Offer']";
			clickLocator(makeAnOfferLocator);
			click("AdvanceRate");
			sendKeys("AdvanceRate", "20");
			click("FactoringRate");
			sendKeys("FactoringRate", "0.12");
			sendKeys("OtherFee", "Processing fee - 5%");
			click("MakeAnOffer");
			waitForInvisibilityOf("MakeAnOffer");
			switchToWindow(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("Supplier views and accepts funding offer")
	public void supplierViewsAndAcceptsFundingOffer() {
	   
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
	
	@When("Supplier signs the funding agreement with OTP")
	public void supplierSignsTheFundingAgreementWithOTP() {

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
			click("UseElectronicSignature");
			click("SignatureNextButton");
			waitForElement(2);
			click("UploadFundingAgreementESign");
			waitForElement(2);
			uploadFile(fundingAgreement);
			waitForElement(5);
			click("CheckBoxEsign");
			waitForElement(2);
			readOTP();
			waitForElement(2);
			log.info("OTP is " + OTP);
			switchToWindow(1);
			click("OTP_TextBox");
			sendKeys("OTP_TextBox", OTP);
			waitForElement(2);
			click("SignNow");
			waitForVisibilityOf("SignedOrVerified");
			waitForElement(6);
			click("CloseAgreementPopupIcon");
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
	
	@Then("Funder marks the transactions as Mark as Paid")
	public void funderMarksTheTransactionsAsMarkAsPaid() {
	   
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
	
	public void readOTP() {

		try {
			driver.switchTo().newWindow(WindowType.WINDOW);
			url(config.getProperty("mailinatorUrl") + funderEmailAddress);
			fluentWaitByXpath(or.getProperty("emailMessage"));
			click("emailMessage");
			waitForElement(1);
			switchToFrame(0);
			waitForElement(3);
			waitForVisibilityOf("OTP_Number");
			OTP = driver.findElement(By.xpath(or.getProperty("OTP_Number"))).getText().trim();
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

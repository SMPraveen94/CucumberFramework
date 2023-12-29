package stepDefinitions;

import applicationController.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class Login2Steps extends BaseTest{
	
	@Given("User navigates to url")
	public void userNavigatesToUrl() {
	   
		driver.get("https://www.gmail.com");	
	}
	
	@Then("User validates the site")
	public void userValidatesTheSite() throws Exception {
	
		System.out.println("title is "+driver.getTitle());	
		Thread.sleep(3000);
	//	driver.quit();
	}
	

}

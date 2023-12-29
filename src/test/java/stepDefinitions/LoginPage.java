package stepDefinitions;

import org.openqa.selenium.*;
import applicationController.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class LoginPage extends BaseTest {

	@Given("User opens the browser")
	public void userOpensTheBrowser() {

		driver.get("https://www.google.com");
	}

	@Then("User enters the input")
	public void userEntersTheInput() throws Exception {

		System.out.println("title is " + driver.getTitle());
		driver.findElement(By.name("q1")).sendKeys("Test");
	//	driver.findElement(By.name("q1")).sendKeys("Test");
		//Thread.sleep(3000);
	//	driver.quit();
	}

}

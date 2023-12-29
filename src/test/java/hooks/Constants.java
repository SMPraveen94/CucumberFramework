package hooks;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import applicationController.*;
import io.cucumber.java.*;

public class Constants {

	private static BaseTest base = new BaseTest();

	@Before
	public static void setUp() {

		try {
			base.startWebDriver();
			base.maximizeWindow();
			base.implicitWait();
			base.pageLoadTimeout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown(Scenario scenario) {

		try {
			if (scenario.isFailed()) {
				BaseTest.ts = (TakesScreenshot)BaseTest.driver;
				byte[] screenshot = BaseTest.ts.getScreenshotAs(OutputType.BYTES);
				scenario.attach(screenshot, "image/png", scenario.getName());
			}
			String status = scenario.getStatus().toString();
			scenario.log(status);
			base.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

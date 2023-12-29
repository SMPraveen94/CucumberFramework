package runner;

import org.testng.annotations.*;
import io.cucumber.testng.*;
import io.cucumber.testng.CucumberOptions.SnippetType;

@CucumberOptions(
		features = {"src/test/java/features/"},
		glue = {"stepDefinitions","hooks"},
		snippets = SnippetType.CAMELCASE,
		tags = "@parallel", 
		plugin = {"pretty",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
				},	
	    monochrome = true, // To read the console output in a readable manner
		dryRun = !true // To check the mapping between feature file and step definitions file
		)

public class TestRunner extends AbstractTestNGCucumberTests {

	@Override
    @DataProvider(parallel = !true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
	
}

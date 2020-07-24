package MyTestRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "classpath:Features",
		glue={"StepDefinitions"}, //the path of the step definition files
		plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"},
		dryRun = false,
		monochrome = true,
		strict = true
		//tags = {"@Smoke"}
		)


public class TestRunner {


}

   //OR Condition : {"@SmokeTest, @End2End"} - Execute Scenario which have any of mentioned Tag
   // AND Condition: {"@SmokeTest", "@End2End"} - Execute Scenario if and if Scenario have all mentioned tags
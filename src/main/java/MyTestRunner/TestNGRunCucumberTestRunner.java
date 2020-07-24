package MyTestRunner;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

//@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/main/java/Features"},
		glue={"stepDefinition"},
		plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"},
		dryRun = false,
		monochrome = true,
		strict = true,
		tags = {"~@Ignore"}
		)


public class TestNGRunCucumberTestRunner {
	DesiredCapabilities capabilities = new DesiredCapabilities();
	private TestNGCucumberRunner testNGCucumberRunner;
	public static WebDriver driver;
	
	@BeforeClass
	@Parameters(value = { "myBrowser"})
	public void setupBrowser(String myBrowser) throws Exception {
		if (System.getProperty("os.name").contains("Window")) {
			if (myBrowser.equalsIgnoreCase("firefox")) {
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\main\\java\\resources\\geckodriver.exe");
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
			} else if (myBrowser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\java\\resources\\chromedriver.exe");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			}
		}

	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	@Test(groups = "cucumber features", description = "Runs Cucumber Features", dataProvider = "features")
	public void feature(CucumberFeatureWrapper cucumberFeature) throws Throwable {
	       testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	    }
	
	
	@DataProvider
	public Object[][] features() {
		 if(testNGCucumberRunner == null){
		        testNGCucumberRunner = new TestNGCucumberRunner(TestNGRunCucumberTestRunner.class);
		    }
	     return testNGCucumberRunner.provideFeatures();
	    }
		

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception{
		driver.quit();
		testNGCucumberRunner.finish();
	}

}

   //OR Condition : {"@SmokeTest, @End2End"} - Execute Scenario which have any of mentioned Tag
   // AND Condition: {"@SmokeTest", "@End2End"} - Execute Scenario if and if Scenario have all mentioned tags
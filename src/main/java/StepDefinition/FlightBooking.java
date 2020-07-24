package StepDefinitions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.Popup;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dto.AirlineDetailsDTO;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.Assert;
import utility.AirlineFareSorter;

public class LoginSteps {
	
	WebDriver driver;
	
	@Before
	public void beforeScenario() 
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\java\\resources\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@After
	public void afterScenario() 
	{
	    driver.quit();
	}
	
	@Given("^I login into the application$")
	public void applicationHomePage() {
		driver.get("https://www.cheapoair.com");
	}

	@Given("I click on oneway trip")
	public void i_click_on_oneway_trip() {
		driver.findElements(By.cssSelector(".custom-control-label")).get(1).click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
	}
	
	@Given("I enter from destination {string} and to destination {string}")
	public void i_enter_from_destination_and_to_destination(String fromDest, String toDest) {
	  driver.findElement(By.id("from0")).clear();
	  driver.findElement(By.id("from0")).sendKeys(fromDest);
	  driver.findElement(By.id("to0")).clear();
	  driver.findElement(By.id("to0")).sendKeys(toDest);
	
	}

	@Given("I enter travel date {string}")
	public void i_enter_travel_date(String journeyDate) {
		driver.findElement(By.id("cal0")).click();

		  String[] journeyDateArr = journeyDate.split(" ");
		 
		  String currentMonth = driver.findElements(By.cssSelector(".calendar__single-month.active .calendar__month")).get(0).getText();
		  currentMonth = currentMonth.split(" ")[0].substring(0,3);
		  int monthCount = diffMonth(journeyDateArr[0], currentMonth); // this gives the number of time to be clicked to get to month to be set in UI
				    
		    if(monthCount < 0) {
		    	driver.findElement(By.cssSelector(".calendar-nav__pre.icon.leftNav")).click();
		    }
		    else {
		    	while(monthCount > 1) {
		    		driver.findElement(By.cssSelector(".calendar-nav__next.icon.rightNav")).click();
		    		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		    		--monthCount;
		    	}
		    }
		    int dateToBeClicked = dateToBeClicked(journeyDateArr[0], Integer.parseInt(journeyDateArr[1]));
		    
		    while(Integer.parseInt(journeyDateArr[1]) != setDatePicker(dateToBeClicked)) {
		    	if(Integer.parseInt(journeyDateArr[1]) > setDatePicker(dateToBeClicked)) {
		    		++dateToBeClicked;
		    	}
		    	else if(Integer.parseInt(journeyDateArr[1]) < setDatePicker(dateToBeClicked)) {
		    		--dateToBeClicked;
		    	}
		    	else if(Integer.parseInt(journeyDateArr[1]) == setDatePicker(dateToBeClicked)) {
		    		break;
		    	}
		    }
		
			
		    WebElement calenderDate = driver.findElements(By.cssSelector(".calendar__single-month.active .month-date.date-space")).get(dateToBeClicked-1);
			    JavascriptExecutor js = (JavascriptExecutor) driver;
			    js.executeScript("arguments[0].click();", calenderDate);
		    
	}

	private int setDatePicker(int dateToBeClicked) {
		int calenderDate = Integer.parseInt(driver.findElements(By.cssSelector(".calendar__single-month.active .month-date.date-space")).get(dateToBeClicked).getAttribute("data-date"));
		return calenderDate;
	}
	
	@When("I click on search")
	public void i_click_on_search() {
	  driver.findElement(By.id("searchNow")).click(); 
	}
	
	@When("I click on shortest tab")
	public void i_click_on_shortest_tab() {
		popupClose();
		WebDriverWait wait = new WebDriverWait(driver, 10); 
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".sort__item.is--duration")));
		element.click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}
	
	@SuppressWarnings("null")
	@Then("I select cheapest shortest evening flight")
	public void i_select_cheapest_shortest_evening_flight() {
		boolean staleElement = true; 
		int flightCount = 1;
		while(staleElement){
		  try{	popupClose();
				List<WebElement> flightEle = driver.findElements(By.cssSelector(".contract__segment.col-sm-8"));
				flightCount = flightEle.size();
		        staleElement = false;
		  } catch(StaleElementReferenceException e){
		    staleElement = true;
		  }
		} 
				
		List<AirlineDetailsDTO> airlineList = new ArrayList<AirlineDetailsDTO>();
		List<AirlineDetailsDTO> airlinePmList = new ArrayList<AirlineDetailsDTO>();

		for(int i=0; i<flightCount; i++) {
			popupClose();
			airlineList.add(new AirlineDetailsDTO(
					driver.findElements(By.cssSelector(".contract-block .airline__name")).get(i).getText().toString(), 
					driver.findElements(By.cssSelector(".contract-block .trip__time.is--depart-source .is--flight-time")).get(i).getText().split(" ")[1],
					Integer.parseInt(driver.findElements(By.cssSelector(".contract-block .fare__amount--block .fare__amount.is--total .currency"))
							.get(i)
							.getText()
							.substring(1)
							.trim()), i));
		}
		Predicate<AirlineDetailsDTO> byTime = airline -> airline.getAirlineTime().equalsIgnoreCase("pm");
		//filtering only the evening flights by "pm"
		airlinePmList = airlineList.stream().filter(byTime).collect(Collectors.toList());
		
		//sorting the list of objects by fare so that the least fare will be at index 0
		AirlineFareSorter airlineFareSorter = new AirlineFareSorter(airlinePmList);	
		List<AirlineDetailsDTO> airlinePmSortedList = airlineFareSorter.getSortedAirlineByFare();
		
		System.out.print("Airline with least fare, fastest and operating in evening is:: " + airlinePmSortedList.get(0).getAirlineName() 
				+" "+"$"+ airlinePmSortedList.get(0).getAirlineFare());
		
		popupClose();
		WebElement element = driver.findElements(By.cssSelector(".cta-1st.select__cta")).get(airlinePmSortedList.get(0).getIndex());
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
		
		//clicking on the select button at index of the 1st object which is having the least fare.
		driver.findElements(By.cssSelector(".cta-1st.select__cta")).get(airlinePmSortedList.get(0).getIndex()).click();
		
	}
	
	@Then("^Close the Browser$")
	public void close_the_Browser(){
	    driver.close(); 
	}
	
	private void popupClose() {
		try {
			driver.findElement(By.xpath("//a[starts-with(@onclick,'javascript:now')]")).click();	
		}
		catch(Exception e) {
		}
	}
	
	enum MonthDays {
		   //Constants with values
		   Jan(31), Feb(28), Mar(31), Apr(30), May(31), Jun(30), Jul(31), Aug(31), Sep(30), Oct(31), Nov(30), Dec(31);
		   //Instance variable
		   private int days;
		   //Constructor to initialize the instance variable
		   MonthDays(int days) {
		      this.days = days;
		   }
		   public int getDays() {
		      return this.days;
		   }
		}
	
	private int diffMonth(String monthToBeSet, String currentMonth) {
		Map<String, Integer> monthMap = new HashMap<>();
		monthMap.put("Jan", 1);
		monthMap.put("Feb", 2);
		monthMap.put("Mar", 3);
		monthMap.put("Apr", 4);
		monthMap.put("May", 5);
		monthMap.put("Jun", 6);
		monthMap.put("Jul", 7);
		monthMap.put("Aug", 8);
		monthMap.put("Sep", 9);
		monthMap.put("Oct", 10);
		monthMap.put("Nov", 11);
		monthMap.put("Dec", 12);
		
		int monthToBeSetNum = monthMap.get(monthToBeSet);
		LocalDate currentDate = LocalDate.now();
		int currentMonthNum = currentDate.getMonthValue();
		String currentMonthExpected = currentDate.getMonth().toString().substring(0,3);
		Assert.assertEquals("current Date not showing correctly in date picker", currentMonthExpected, currentMonth.toUpperCase());
		return monthToBeSetNum - currentMonthNum;
	}
	private int dateToBeClicked(String monthToBeSet, int date) {
		Map<String, Integer> monthMap = new HashMap<>();
		monthMap.put("Jan", 1);
		monthMap.put("Feb", 2);
		monthMap.put("Mar", 3);
		monthMap.put("Apr", 4);
		monthMap.put("May", 5);
		monthMap.put("Jun", 6);
		monthMap.put("Jul", 7);
		monthMap.put("Aug", 8);
		monthMap.put("Sep", 9);
		monthMap.put("Oct", 10);
		monthMap.put("Nov", 11);
		monthMap.put("Dec", 12);
		
		LocalDate currentDate = LocalDate.now();	
		if(currentDate.getMonthValue() == monthMap.get(monthToBeSet)) {
			return date; // its the same month so return same date
		}
		else {
			return currentDate.lengthOfMonth() + date; // this would return the previous month days + current month date
		}
	}
}

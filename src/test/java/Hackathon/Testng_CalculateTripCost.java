package Hackathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Testng_CalculateTripCost {
	
	//public static WebDriver driver;
	public static ExtentTest extentTest;
	public static RemoteWebDriver driver;
	
	@BeforeSuite
	//This function is used to initialize the drivers.
	public void SetDrivers() throws IOException {
		//Creating an object of DriverSetup class to use the different drivers of different browsers.
		DriverSetup obj = new DriverSetup();

		//Setting up the driver for chosen browser from properties file
		if(getPropertyFile("Browser").equalsIgnoreCase("Chrome")) {
			
			//driver = (RemoteWebDriver) obj.setupChromeDriver();
			
			//For Selenium Grid we will enable these two lines
			DesiredCapabilities capabilities = DesiredCapabilities.chrome(); //customize the desired capabilities for the browser
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities); 
			
			
			driver.manage().window().maximize(); // maximizes window
			driver.manage().deleteAllCookies(); //delete cookies
	
		}
		
		else if(getPropertyFile("Browser").equalsIgnoreCase("FireFox")) {
			
			driver = (RemoteWebDriver) obj.setupFireFoxDriver();
			
			//For Selenium Grid we will enable these two lines
			//DesiredCapabilities capabilities = DesiredCapabilities.firefox(); //customize the desired capabilities for the browser
			//driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities); 
			driver.manage().window().maximize(); // maximizes window
			driver.manage().deleteAllCookies(); //delete cookies
			
		}
		else if(getPropertyFile("Browser").equalsIgnoreCase("Opera")) {
			
			driver = (RemoteWebDriver) obj.setupOperaDriver();
			
			//For Selenium Grid we will enable these two lines
			//DesiredCapabilities capabilities = DesiredCapabilities.opera(); //customize the desired capabilities for the browser
			//driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities); 
			driver.manage().window().maximize(); // maximizes window
			driver.manage().deleteAllCookies(); //delete cookies
			
		}
		else {
			
			driver = (RemoteWebDriver) obj.setupChromeDriver();
			
			//For Selenium Grid we will enable these two lines
			//DesiredCapabilities capabilities = DesiredCapabilities.chrome(); //customize the desired capabilities for the browser
			//driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities); 
			
			driver.manage().window().maximize(); // maximizes window
			driver.manage().deleteAllCookies(); //delete cookies
			
		}
		
		//Creating an object of ExtentReport to log the status
		ExtentReport ext = new ExtentReport();
		extentTest = ext.Extent_Report();
			
	}

	//This function is used to read the property file
	public static String getPropertyFile(String prop_name) throws IOException {
		
		Properties prop =new Properties(); 
		//Importing the config file
		InputStream input= new FileInputStream(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\Config.properties");
		//Loading the config file
		prop.load(input);
		return prop.getProperty(prop_name);
		
	}
	
	//This function is used to take screenshots
	public void TakeScreenShot(String Name) {
		
		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		//Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		//Move image file to new destination
		String path = System.getProperty("user.home");
		File DestFile = new File(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\" + Name +".png");
		//Copy file at destination
		try {
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test (priority = 0)
	//This function to to set the url 
	public void SetUrl() throws IOException {
		
		String Base_Url = getPropertyFile("Url");
		driver.get(Base_Url);
		//This code is used to maximize the window
		driver.manage().window().maximize();
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Navigated to https://www.tripadvisor.in/");
		
		//To take a screenshot of the Home Page
		TakeScreenShot("HomePage");
		//Adding Extent Report
		String extentReportImage1 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\HomePage.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Trip Advisor Home Page : "+ extentTest.addScreenCapture(extentReportImage1));
		
		
	}
	
	@Test (priority = 1)
	//This function is to search the Destination
	public void SearchDestination() throws IOException, InterruptedException {

		//Creating an object of the Input Excel Class
		ReadUserDetailsExcel Excel_input = new ReadUserDetailsExcel();
		String destination = Excel_input.ReadDestination();
		
		//Creating an object of SearchDestination to search our desired Destination
		SearchDestination Search = new SearchDestination();
		Search.SearchDest(driver, destination);
		//Adding Extent Report
		String extentReportImage = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Search_Suggestions.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Search Suggestions : "+ extentTest.addScreenCapture(extentReportImage));
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Entered the Search Destination");
		
		//Printing to console
		System.out.println("Destination is: "+destination);
		
	}
	
	@Test (priority = 2, dependsOnMethods = "SearchDestination")
	//To Click on the Holiday Homes Button
	public void ClickHolidayHomesButton() throws InterruptedException {
		
		//We wait for the element to appear until the page loads
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebDriverWait wait=new WebDriverWait(driver, 30);
		
		//We check if the element is present or it is clickable
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Holiday Homes")));
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Holiday Homes")));

		//Click on the holiday homes button 
		WebElement Holiday_homes = driver.findElement(By.partialLinkText("Holiday Homes"));
		Actions actions = new Actions(driver);
		//To take a screenshot of Holiday Homes button
		TakeScreenShot("HolidayHomes");
		
		//Adding Extent Report
		String extentReportImage2 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\HolidayHomes.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Holiday Home Button : "+ extentTest.addScreenCapture(extentReportImage2));
		
		//Clicking on Holiday Homes
		actions.moveToElement(Holiday_homes).doubleClick().build().perform();
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Holiday Homes Button Clicked");
		
	}
	
	@Test (priority = 3)
	//To set the Check-in and Check-out Dates
	public void SetDate() throws IOException, InterruptedException {
		
		//Creating an object of the Input Excel Class
		ReadUserDetailsExcel Excel_input = new ReadUserDetailsExcel();
		String[] Dates = new String[2];
		Dates = Excel_input.ReadDates();
		//Selecting the Check in and Check out Date from the Array Dates
		String check_in = Dates[0];
		String check_out = Dates[1];
		//Creating an object of SetDates, to set the check-in and check-out dates
		SetDates setDate = new SetDates();
		setDate.SetCheckInDate(driver, check_in);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		setDate.SetCheckOutDate(driver, check_out);
		//Adding Extent Report
		String CheckInDate = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Check_in.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Check in date entered : "+ extentTest.addScreenCapture(CheckInDate));
		
		String CheckOutDate = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Check_out.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Check iout date entered : "+ extentTest.addScreenCapture(CheckOutDate));
		
		
		//Printing to console
		System.out.println("Check-in Date is : "+check_in+" Check-out Date is: "+check_out);
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Check-in and Check-out Date entered");
		
	}
	
	@Test (priority = 4)
	//To select the Number of Guests
	public void NoOfGuests() throws Exception {

		//Wait for the page to load
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    //Clicking on Guests
	    driver.findElement(By.className("_2uJXqhFj")).click();
	        
	    //Creating an object of the Input Excel Class
	    ReadUserDetailsExcel Excel_input = new ReadUserDetailsExcel();
	    int number_of_guests = Excel_input.ReadNumberOfGuest();
	    
	    //as default value is 2, so we will increment till the amount of guests we need
	    for(int defvalue=2 ; defvalue<number_of_guests ; defvalue++){
	    	 
	        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	        driver.findElement(By.cssSelector(getPropertyFile("Guest_add"))).click();
	        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);    
	        
	    }
		//To take a screenshot
		TakeScreenShot("NoOfGuest");
		//Adding Extent Report
		String extentReportImage3 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\NoOfGuest.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Number of Guests : "+ extentTest.addScreenCapture(extentReportImage3));
		
	    driver.findElement(By.cssSelector(getPropertyFile("Guest_add_apply"))).click();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    
		//Printing to console
		System.out.println("Number of guests to book Holiday Homes is :  "+number_of_guests);
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Number of guests added");
	        
	  }
	
	@Test(priority = 5)
	//To sort the results by Traveler Ratings
	public void RatingSort() throws Exception{
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);	
		//We check if the Traveller Rating Dropdown is present or it is clickable
		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(getPropertyFile("Sort_rate"))));
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(getPropertyFile("Sort_rate"))));
	    
	    //Sorting according to Traveller Rating Sort
	    driver.findElement(By.cssSelector(getPropertyFile("Sort_rate"))).click();
	    driver.findElement(By.xpath("//span[contains(text(),'Traveller Rating')]")).click();
	        
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//To take a screenshot
		TakeScreenShot("Traveler Sort");
		//Adding Extent Report
		String extentReportImage4 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Traveler Sort.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Sorting Selected : "+ extentTest.addScreenCapture(extentReportImage4));
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Sorted by Travelers Rating");
		
	 }
	 
	@Test( priority = 6)
	//To click on LiftAaccess
	public void LiftAccess() throws Exception
	{
		
		//Selecting Elevator/Lift access in amenities
	    JavascriptExecutor js =(JavascriptExecutor)driver;
	    js.executeScript("window.scrollBy(0,1100)");
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//Clicking on dropdown in Amenities to select Lift/Elevator access
	    driver.findElement(By.cssSelector(getPropertyFile("Aminities"))).click();
	    
	    driver.findElement(By.xpath("//div[contains(text(),'Elevator/Lift access')]")).click();
	    
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//To take a screenshot
		TakeScreenShot("Lift Access");
		//Adding Extent Report
		String extentReportImage5 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Lift Access.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Lift Access Selected : "+ extentTest.addScreenCapture(extentReportImage5));

	    js.executeScript("window.scrollBy(0,-900)");
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    //To sync the code
	    Thread.sleep(1000);
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "LiftAccess from Amminities chosen");
		
	}
	
	@Test (priority = 7)
	//To get all the required details of the top-3 result hotels
	public void HotelDetails() throws IOException, InterruptedException {
		
		//We wait for the elements to load and to be clickable
		WebElement Hotel_name = driver.findElements(By.cssSelector("._2K0y-IXo")).get(0);
		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getPropertyFile("Search_Result")))));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(getPropertyFile("Search_Result"))));
		
		//To take a screenshot
		TakeScreenShot("Search Results");
		//Adding Extent Report
		String extentReportImage6 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Search Results.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of Search Results : "+ extentTest.addScreenCapture(extentReportImage6));
		
		//Creating an object of GetHotelDetails to fetch the name, charges per night and total cost
		GetHotelDetails GetHotels = new GetHotelDetails();
		
		//To store the Hotel Names
		String[] name = new String[3];
		name = GetHotels.GetHotelName(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//To store the total amount
		String[] total_amount = new String[3];
		total_amount = GetHotels.GetHotelTotalCharge(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//To store the charges per night
		String[] charges_pernight = new String[3];
		charges_pernight = GetHotels.GetChargesPerNight(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//Creating an object of WriteHotelDetails to send the above data to excel
		WriteHotelDetails SendToExcel = new WriteHotelDetails();
		SendToExcel.WriteToExcel(name, total_amount, charges_pernight);
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Search Results Shown");
	}
	
	@Test (priority = 8)
	//To get the screenshots of the 3 respective Hotels
	public void GetHotelScreenShots() throws IOException, InterruptedException {
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//Creating an object of GetHotelDetails to fetch the Hrefs of the Hotels
		GetHotelDetails GetHref = new GetHotelDetails();
		
		//To store the Hrefs
		String[] Hotel_Hrefs = new String[3];
		Hotel_Hrefs = GetHref.GetHotelLinks(driver);
		System.out.println("Size is :" + Hotel_Hrefs.length);
		for(int i = 0 ; i < Hotel_Hrefs.length; i++) {
			
			System.out.println(Hotel_Hrefs[i]);
			driver.get(Hotel_Hrefs[i]);
			//To take a screenshot
			TakeScreenShot("Hotel + "+i);
			
			//Using ExtentReport to log the Status
			extentTest.log(LogStatus.INFO, "Hotels from Search Result Number: "+i);
			
			//Adding Extent Report
			String extentReportImage6 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\+Hotel + "+i+".png";
			extentTest.log(LogStatus.INFO,"ScreenShot of Search Resuslts's Hotels : "+ extentTest.addScreenCapture(extentReportImage6));
			
		}	
		//Using ExtentReport to log the Status
	}
	
	@Test (priority = 9)
	//To set the Cruise Ship and Cruise Line
	public void SetCruises() throws IOException, InterruptedException {
		
		System.out.println("Now switching to Cruise Section");
		//Creating an object of ReadCruiseDetailsExcel to read the Cruise Details from Excel
		ReadCruiseDetailsExcel ReadExcel = new ReadCruiseDetailsExcel();
		List<String> CruiseDetails = new ArrayList<String>();
		CruiseDetails = ReadExcel.ReadExcelWords();
		
		//Storing the cruiseship and cruiseline in a string
		String CruiseShip = CruiseDetails.get(0);
		String CruiseLine = CruiseDetails.get(1);
		
		//Now we set the drivers to go to cruise
		SetCruiseDetails Cruise = new SetCruiseDetails();
		driver = (RemoteWebDriver) Cruise.setDriver(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//Setting the values and clicking on submit
		Cruise.SetCruiseShip(driver, CruiseShip);
		//To take a screenshot
		Thread.sleep(300);
		TakeScreenShot("CruiseShip");
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Cruise-Ship selected");
		
		//Adding Extent Report
		String extentReportImage7 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\CruiseShip.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of CruiseShip : "+ extentTest.addScreenCapture(extentReportImage7));
		
		Cruise.SetCruiseLine(driver, CruiseLine);
		//To take a screenshot
		Thread.sleep(300);
		TakeScreenShot("CruiseLine");
		
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Cruise-Line selected");
		
		//Adding Extent Report
		String extentReportImage8 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\CruiseLine.png";
		extentTest.log(LogStatus.INFO,"ScreenShot of CruiseLine : "+ extentTest.addScreenCapture(extentReportImage8));
		
		//To sync the code
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//Click on search
		Cruise.SearchCruise(driver);
		
		//To print to console
		System.out.println("CruiseShips chosen is: "+CruiseShip+" CruiseLine chosen is: "+CruiseLine);
	}
	
	@Test (priority = 10, dependsOnMethods = "SetCruises")
	//To get the Cruise Details
	public void GetCruiseDetails() throws IOException, InterruptedException {
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//Creating an excel file to write data
		WriteCruiseDetails excel=new WriteCruiseDetails();
        
        //Get the window handle of current window
        String parent=driver.getWindowHandle();
        
        //Creating object of Passengers and Crew class to write data in excel
        PassengersAndCrew passenger=new PassengersAndCrew(driver);
        passenger.passengersAndCrew(parent,excel);
        //To take a screenshot
        Thread.sleep(300);
      	TakeScreenShot("Availaible Cruise Options");
      	
      	//Using ExtentReport to log the Status
      	extentTest.log(LogStatus.INFO, "All details from cruise are shown");
      	
      	//Adding Extent Report
      	String extentReportImage9 = System.getProperty("user.home") + "\\eclipse-workspace\\CalculateTripCost\\ScreenShots\\Availaible Cruise Options.png";
      	extentTest.log(LogStatus.INFO,"ScreenShot of Availaible Cruise Options : "+ extentTest.addScreenCapture(extentReportImage9));
        
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //Creating object of LaunchedYear class to write data in excel
        LaunchedYear year=new LaunchedYear(driver);
        year.launchedYear(parent,excel);
        
        //Creating object of Languages class to write data in excel
        Languages lang=new Languages(driver);
        lang.languages(parent,excel);
		
	}
	
	@AfterMethod //AfterMethod annotation - This method executes after every test execution
	public void screenShot(ITestResult result){
	//using ITestResult.FAILURE is equals to result.getStatus then it enter into if condition
		if(ITestResult.FAILURE==result.getStatus()){
			try{
				// To create reference of TakesScreenshot
				TakesScreenshot screenshot=(TakesScreenshot)driver;
				// Call method to capture screenshot
				File src=screenshot.getScreenshotAs(OutputType.FILE);
				// Copy files to specific location 
				// result.getName() will return name of test case so that screenshot name will be same as test case name
				FileUtils.copyFile(src, new File(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\Error_Screenshots\\"+result.getName()+".png"));
				System.out.println("Successfully captured a screenshot");
			}catch (Exception e){
				System.out.println("Exception while taking screenshot "+e.getMessage());
			} 
		}
	}
	
	@AfterSuite
	public void CloseBrowser() {
		
		//To quit the browser
		driver.quit();
		//Using ExtentReport to log the Status
		extentTest.log(LogStatus.INFO, "Drivers successfully closed");
		
		//creating an object of ExtentReport to end Extent Report
		ExtentReport ext = new ExtentReport();
		ext.endExtentTest();
		
	}
	
}



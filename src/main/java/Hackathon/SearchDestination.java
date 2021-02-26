package Hackathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchDestination {

	public void TakeScreenShot(WebDriver driver, String Name) {
		
		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		//Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		//Move image file to new destination
		String path = System.getProperty("user.home");
		File DestFile = new File(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\Screenshots\\" + Name +".png");
		//Copy file at destination
		try {
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void SearchDest(WebDriver driver, String Location) throws IOException {

		//Creating an object of property file
		Properties prop =new Properties(); 
		//Importing the config file
		InputStream input= new FileInputStream(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\Config.properties");
		//Loading the config file
		prop.load(input);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		//Clicking on the search button
		jse.executeScript("document.getElementsByClassName('"+prop.getProperty("Search_Result_Class_Name")+"')[1].click()");
		
		//Sending the data from Excel File to the Search Field
		driver.findElements(By.cssSelector(prop.getProperty("Search_Result_Class_Name_Css"))).get(1).sendKeys(Location);
		
		//To Load the other elements, we need to scroll down
		jse.executeScript("window.scrollBy(0,350)");
		//We wait until all the elements are loaded in the search suggestions
		WebDriverWait wait=new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("SearchSuggestion"))));
		//Now we fetch the suggestions and store inside a list
		WebElement Parent = driver.findElement(By.className(prop.getProperty("Search_Suggestion_Class_Name")));
		//To take a screenshot
		TakeScreenShot(driver, "Search_Suggestions");
		List<WebElement> Search_Suggestions=new ArrayList<WebElement>(); 
		Search_Suggestions = Parent.findElements(By.tagName("a"));
		int temp = 0;
		//Here we check if the suggestions shown matches our desired suggestion
		for(int i = 0 ; i < Search_Suggestions.size(); i++) {
			
			String title = Search_Suggestions.get(i).getAttribute("href");
			System.out.println(title);
			//Now we are checking if the search suggestions href contains the href of the page we want
			try {
				if(Search_Suggestions.get(i).getAttribute("href").equalsIgnoreCase("https://www.tripadvisor.in/Tourism-g294207-Nairobi-Vacations.html")) 
				{
					temp = i;
					break;
				}
			}
			catch(Exception e) {
				System.out.println("Search-Suggestions Not available");
				driver.get("https://www.tripadvisor.in/Tourism-g294207-Nairobi-Vacations.html");
			}
		}
		//Navigating to the href that we got from Search Suggestions
		driver.get(Search_Suggestions.get(temp).getAttribute("href"));
		
	}
	
}

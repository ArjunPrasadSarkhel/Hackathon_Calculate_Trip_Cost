package Hackathon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SetCruiseDetails {
	
	public WebDriver setDriver(WebDriver driver) throws IOException {
		
		Properties prop =new Properties(); 
		//Importing the config file
		InputStream input= new FileInputStream(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\Config.properties");
		//Loading the config file
		prop.load(input);
		driver.get(prop.getProperty("Cruise_Url"));	
		return driver;
		
	}
	
	public void SetCruiseShip(WebDriver driver, String CruiseShip) {
		
		WebDriverWait wait=new WebDriverWait(driver, 30);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cruise_line_dropdown")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("cruise_line_dropdown")));

		List<WebElement> Options=new ArrayList<WebElement>(); 
		driver.findElements(By.cssSelector(".ollHTlS0")).get(0).click();
		
		WebElement Parent = driver.findElement(By.cssSelector("div[class='_16IExTAJ _1S9IhgUs _2QtYWK6H']"));
		
		Options = Parent.findElements(By.tagName("div"));
		for(int i = 0 ; i < Options.size(); i++) {
			
			if(Options.get(i).getText().equalsIgnoreCase(CruiseShip)) {
				
				driver.findElement(By.cssSelector("div[class='_16IExTAJ _1S9IhgUs _2QtYWK6H'] div:nth-child("+i+")")).click();
				break;
			}
			
		}
		
	}
	
	
	public void SetCruiseLine(WebDriver driver, String CruiseLine) {
		
		List<WebElement> Options=new ArrayList<WebElement>(); 
		
		driver.findElements(By.cssSelector(".ollHTlS0")).get(1).click();
		
		WebElement Parent = driver.findElement(By.cssSelector("div[class='_16IExTAJ _1S9IhgUs _2QtYWK6H']"));
		
		Options = Parent.findElements(By.tagName("div"));
		int temp = 0;
		for(int i = 0 ; i < Options.size(); i++) {
			
			if(Options.get(i).getText().equalsIgnoreCase(CruiseLine)) {
		
				temp = i-2;
				driver.findElement(By.cssSelector("div[class='_16IExTAJ _1S9IhgUs _2QtYWK6H'] div:nth-child("+temp+")")).click();
				break;
			}
			
		}
		
	}
	
	public void SearchCruise(WebDriver driver) throws InterruptedException {

		Thread.sleep(1000);
		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("._32M3JNKp")));
	    driver.findElement(By.cssSelector("span._2O1ErRJV:nth-child(1) > button")).click();

	}
	
	

}

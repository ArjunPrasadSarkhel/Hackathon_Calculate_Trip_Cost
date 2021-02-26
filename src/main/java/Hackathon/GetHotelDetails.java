package Hackathon;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetHotelDetails {
	
	//This function is used to read the property file
	public static String getPropertyFile(String prop_name) throws IOException {
			
		Properties prop =new Properties(); 
		//Importing the config file
		InputStream input= new FileInputStream(System.getProperty("user.home")+"\\eclipse-workspace\\CalculateTripCost\\Config.properties");
		//Loading the config file
		prop.load(input);
		return prop.getProperty(prop_name);
			
	}
	
	//This function is used to fetch the Hotel Names
	public static String[] GetHotelName(WebDriver driver) throws IOException {

		List<WebElement> Hotel_Names=new ArrayList<WebElement>();
		
		//Getting the parent WebElement of all the search results
		WebElement Parent = driver.findElement(By.className(getPropertyFile("Hotel_Parent")));
		Hotel_Names = Parent.findElements(By.xpath("//h2[contains(@class='_2K0y-IXo', '')]"));
		
		//Creating a String array to store the Hotel Names of the First 3 Search Results
		String[] HotelName = new String[3];
		//To print to console
		System.out.println("Hotel Names are:----     ");
		for(int i = 0 ; i < 3; i++) {
			//Storing hotel names inside our array
			HotelName[i] = Hotel_Names.get(i).getText();
			System.out.println(Hotel_Names.get(i).getText());
		}
		
		return HotelName;
		
	}
	
	//This function is used to fetch the Total Hotel Charges
	public static String[] GetHotelTotalCharge(WebDriver driver) throws IOException {
		
		List<WebElement> Total_Charges=new ArrayList<WebElement>();
		
		//Getting the parent WebElement of all the search results
		WebElement Parent = driver.findElement(By.className(getPropertyFile("Hotel_Parent")));
		Total_Charges = Parent.findElements(By.className(getPropertyFile("Hotel_Charges")));
		
		//Creating a String array to store the Total Charges of the First 3 Search Results
		String[] TotalCharge = new String[3];
		//To print to console
		System.out.println("Total charges are:----     ");
		for(int i = 0 ; i < 3; i++) {
			//Storing hotel names inside our array
			TotalCharge[i] = Total_Charges.get(i).getText();
			System.out.println(Total_Charges.get(i).getText());		
		}
		
		return TotalCharge;
			
	}
	
	//This function is to fetch the Hotel Charges per night
	public static String[] GetChargesPerNight(WebDriver driver) throws IOException {
		
		List<WebElement> ChargesPerNight=new ArrayList<WebElement>();
		
		//Getting the parent WebElement of all the search results
		WebElement Parent = driver.findElement(By.className(getPropertyFile("Hotel_Parent")));
		ChargesPerNight = Parent.findElements(By.className(getPropertyFile("Hotel_PerNight")));
		//To print to console
		System.out.println("Charges per night are:----     ");
		//Creating a String array to store the Charges per Night of the First 3 Search Results
		String[] TotalCharge = new String[3];
		for(int i = 0 ; i < 3; i++) {
			
			//Storing hotel names inside our array
			TotalCharge[i] = ChargesPerNight.get(i).getText();
			System.out.println(ChargesPerNight.get(i).getText());		
		}
		
		return TotalCharge;
			
	}
	
	//This function is to get the Hotel Hrefs
	public static String[] GetHotelLinks(WebDriver driver) throws IOException, InterruptedException {
		
		List<WebElement> Hrefs=new ArrayList<WebElement>();  
		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getPropertyFile("Search_Result")))));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(getPropertyFile("Search_Result"))));
		Thread.sleep(500);
		
		//Getting the parent WebElement of all the search results
		WebElement Parent = driver.findElement(By.className(getPropertyFile("Hotel_Parent")));
		Hrefs = Parent.findElements(By.xpath("//h2[contains(@class='_2K0y-IXo', '')]/a"));
		
		//Creating a String array to store the Href Links of the First 3 Search Results
		String[] Href = new String[3];
		for(int i = 0; i < 3; i++) {
			
			Href[i] = Hrefs.get(i).getAttribute("href");

		}
		
		return Href;
	}

}

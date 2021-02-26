package Hackathon;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;

public class DriverSetup {
	
	private static WebDriver driver;
	
	//This Function is used to setup Chrome Driver
	public static WebDriver setupChromeDriver(){
		//Here, we are using path to get the home directory of the user to setup our webdriver.
		String path = System.getProperty("user.home");
		System.setProperty("webdriver.chrome.driver", path + "\\eclipse-workspace\\CalculateTripCost\\Drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		return driver;
		
	}
	
	//This Function is used to setup FireFox Driver
	public static WebDriver setupFireFoxDriver() {
		//Here, we are using path to get the home directory of the user to setup our webdriver.
		String path = System.getProperty("user.home");
		System.setProperty("webdriver.gecko.driver", path + "\\eclipse-workspace\\CalculateTripCost\\Drivers\\geckodriver.exe");
		driver = new FirefoxDriver();
		return driver;
		
	}
	
	//This Function is used to setup Opera Driver
	public static WebDriver setupOperaDriver() {
		//Here, we are using path to get the home directory of the user to setup our webdriver.
		String path = System.getProperty("user.home");
		System.setProperty("webdriver.opera.driver", path + "\\eclipse-workspace\\CalculateTripCost\\Drivers\\operadriver.exe");
		driver = new OperaDriver();
		return driver;
		
	}
	
}

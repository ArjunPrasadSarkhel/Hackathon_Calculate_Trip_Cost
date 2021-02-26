package Hackathon;

import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class LaunchedYear {
	 
    public static WebDriver driver;
 
    public LaunchedYear(WebDriver d) {
        driver = d;
    }
 
    // To write Launched year in excel file
    public void launchedYear(String parent,WriteCruiseDetails excel) throws IOException {
        
        JavascriptExecutor jse=(JavascriptExecutor)driver;
        
        
        
        Set<String> s = driver.getWindowHandles();
        for (String element : s) {
            if (!parent.equals(element)) {
                driver.switchTo().window(element);
            }
        }
        
        
        jse.executeScript("window.scrollBy(0,350)");
        
 
        excel.writeToExcel(driver.findElement(By.xpath("//*[contains(text(),'Launched')]")).getText());
    }       
}
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;


public class second {

	static WebDriver driver;
	static String baseUrl;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		startBrowser();
		getDataToList();
		driver.close();
		
	}
	
	public static void startBrowser() {
		
		baseUrl = "http://www.bing.com/";
		FirefoxProfile profile = new FirefoxProfile();
		String locale;
		locale = changeLocale();
		profile.setPreference("intl.accept_languages", locale);
		driver = new FirefoxDriver(profile);
		driver.get(baseUrl);
	}
	
	static String changeLocale() {
		
		int i = (int)(Math.random()*10);
		switch (i) {
			case 0: return "en";
			case 1: return "fr";
			case 2: return "de";
			case 3: return "es";
			case 4: return "lt";
			case 5: return "uk";
			case 6: return "ja";
			case 7: return "cs";
			case 8: return "ru";
			case 9: return "fy";
			default: return "en";
		}
	}
	
	static void getDataToList() {
		
		List<WebElement> links = driver.findElements(By.xpath(".//*/a"));
		for (WebElement link : links) {
			if (link.isDisplayed() == true & !(link.getText().isEmpty()) )
				System.out.println(link.getAttribute("h") + " : " + link.getText());
		}
	}
}

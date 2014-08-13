import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;


public class second {

	static WebDriver driver;
	static String locale = "en";
	static String baseUrl = "http://www.bing.com/";
	static List<trans> EnglishElements = new ArrayList<trans>();
	static List<trans> LocalizedElements = new ArrayList<trans>();
	
	
	public static void main(String[] args) throws IOException {
		
		startBrowser(locale);
		getDataToList(EnglishElements);
		driver.close();
		
		try (
				FileOutputStream OutPutFile = new FileOutputStream("bing.txt");	
				DataOutputStream of = new DataOutputStream(OutPutFile);	){
		
					for (int i=0; i<10; i++) {
						locale = changeLocale(i);
						startBrowser(locale);
						getDataToList(LocalizedElements);
						driver.close();
						compairing(of);
					}
		}
	}
	
	private static void compairing(DataOutputStream of) throws IOException {
		System.out.println("----- Locale is " + locale + "-----");
		of.writeChars("----- Locale is " + locale + "-----");	
		
		AbstractMap<String, String> locList = new TreeMap<String, String>();
		
		for (trans t : LocalizedElements) {
			locList.put(t.getId(), t.getData());
		}
		
		for (trans element: EnglishElements) {
						
			if (element.getData().compareTo(locList.get(element.getId()).toString()) == 0)
				{
				System.out.print("!!! ");
				of.writeChars("!!! ");
				}
			
			System.out.println(locList.get(element.getId()));
			of.writeUTF(locList.get(element.getId()));
			of.writeUTF("\r\n");
			
		}
		
	}

	public static void startBrowser(String locale) {
				
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", locale);
		driver = new FirefoxDriver(profile);
		driver.get(baseUrl);
	}
		
	static String changeLocale(int i) {
					
			switch (i) {
				case 0: return "nl";
				case 1: return "fr";
				case 2: return "de";
				case 3: return "es";
				case 4: return "lt";
				case 5: return "uk";
				case 6: return "ja";
				case 7: return "cs";
				case 8: return "ru";
				case 9: return "fy";
				default: return "ru";
			}
		}
	
	static void getDataToList(List<trans> Elements) {
		
		List<WebElement> baseList = driver.findElements(By.xpath(".//*/a"));
				
		for (WebElement link : baseList) {
			if (link.isDisplayed() == true & !(link.getText().isEmpty()) ) {
					Elements.add(new trans(link.getAttribute("h"), link.getText()));
				}
		}
	}

}

class trans {
	String id;
	String data;
	
	trans (String id, String data) {
		this.id = id;
		this.data = data;
	}
	
	String getId(){
		return id;
	}
	
	String getData(){
		return data;
	}
}
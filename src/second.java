import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class second {

	static WebDriver driver;
	static String locale;
	static List<WebElement> links;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		startBrowser();
		getDataToList();
		writeToXml();
		driver.close();
		
	}
	
	public static void startBrowser() {
		
		String baseUrl = "http://www.bing.com/";
		FirefoxProfile profile = new FirefoxProfile();
		
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
		
		links = driver.findElements(By.xpath(".//*/a"));
		for (WebElement link : links) {
			if (link.isDisplayed() == true & !(link.getText().isEmpty()) )
				System.out.println(link.getAttribute("h") + " : " + link.getText());
		}
	}
	
	static void writeToXml() {
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 		Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement(locale);
			doc.appendChild(rootElement);
						
			Element page = doc.createElement("HomePage");
			rootElement.appendChild(page);
			
			System.out.println("Write to file. Current locale is: " + locale);
			String attribute;						
			for (WebElement link : links) {
				if (link.isDisplayed() == true & !(link.getText().isEmpty()) ) {
					
					attribute = link.getAttribute("h");
					attribute = attribute.replace(',', '.');
					attribute = attribute.replace('=', '.');
							
					System.out.println(attribute + " * " + link.getText());
					page.setAttribute(attribute, link.getText());
								
				}
							
			}
		
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("languages1.xml"));
		 						 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
				System.out.println("----------------------------");
		
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
}

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
	static String locale = "en";
	static String baseUrl = "http://www.bing.com/";
	static List<trans> EnglishElements = new ArrayList<trans>();
	static List<trans> LocalizedElements = new ArrayList<trans>();
	
	public static void main(String[] args) {
		
		startBrowser(locale);
		getDataToList(EnglishElements);
		driver.close();
		
		for (int i=0; i<10; i++) {
			locale = changeLocale(i);
			startBrowser(locale);
			getDataToList(LocalizedElements);
			driver.close();
			compairing();
		}
		
	}
	
	private static void compairing() {
		System.out.println("----- Locale is " + locale + "-----");
		
		AbstractMap<String, String> locList = new TreeMap<String, String>();
		for (trans t : LocalizedElements) {
			locList.put(t.getId(), t.getData());
		}
		
		for (trans element: EnglishElements) {
						
			if (element.getData().compareTo(locList.get(element.getId()).toString()) == 0)
				System.out.print("!!! ");
			System.out.println(locList.get(element.getId()));
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
	
	static void writeToXml() {
//		try {
//			
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//	 		Document doc = docBuilder.newDocument();
//			
//			Element rootElement = doc.createElement(locale);
//			doc.appendChild(rootElement);
//						
//			Element page = doc.createElement("HomePage");
//			rootElement.appendChild(page);
//			
//			System.out.println("Write to file. Current locale is: " + locale);
//			String attribute;						
//			for (WebElement link : links) {
//				if (link.isDisplayed() == true & !(link.getText().isEmpty()) ) {
//					
//					attribute = link.getAttribute("h");
//					attribute = attribute.replace(',', '.');
//					attribute = attribute.replace('=', '.');
//							
//					System.out.println(attribute + " * " + link.getText());
//					page.setAttribute(attribute, link.getText());
//								
//				}
//							
//			}
//		
//				TransformerFactory transformerFactory = TransformerFactory.newInstance();
//				Transformer transformer = transformerFactory.newTransformer();
//				DOMSource source = new DOMSource(doc);
//				StreamResult result = new StreamResult(new File("languages1.xml"));
//		 						 
//				transformer.transform(source, result);
//		 
//				System.out.println("File saved!");
//				System.out.println("----------------------------");
//		
//		} catch (ParserConfigurationException pce) {
//			pce.printStackTrace();
//		  } catch (TransformerException tfe) {
//			tfe.printStackTrace();
//		  }
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
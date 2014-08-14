import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
	static List<trans> EnglishElements = new ArrayList<trans>();	//list of English elements for compare with
	static List<trans> LocalizedElements = new ArrayList<trans>();	//list for temporary keeping current locale's elements 
	static Document doc;
	static Element rootElement;
	
	
	public static void main(String[] args) throws IOException {
		
		startBrowser(locale); //start with English locale and create base for comparing
		getDataToList(EnglishElements); //filling list of English elements
		driver.close();
		
		try (
				FileOutputStream OutPutFile = new FileOutputStream("bing.txt");	//file for log
				DataOutputStream of = new DataOutputStream(OutPutFile);){
			
			try {	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			 		doc = docBuilder.newDocument();
			 		
			 		rootElement = doc.createElement("HomePage");
					doc.appendChild(rootElement);
				
						for (int i=0; i<10; i++) {	//select different locales and check translation on each of them
							locale = changeLocale(i);
							startBrowser(locale);
							getDataToList(LocalizedElements);
							driver.close();
							compairing(of);	//out the result to console and log file 
							writeToXml();
						}
				
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
						StreamResult result = new StreamResult(new File("languages2.xml"));
				 						 
						transformer.transform(source, result);
						
			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			  } catch (TransformerException tfe) {
				  tfe.printStackTrace();
			}
					
		}
	}
	
	private static void compairing(DataOutputStream of) throws IOException {	// if localized string is equal to English it marked with "!!!"
		System.out.println("----- Locale is " + locale + "-----");
		of.writeChars("----- Locale is " + locale + "-----");
		of.writeUTF("\r\n");
		
		AbstractMap<String, String> locList = new TreeMap<String, String>();	// This map for getting data according to English data list 
		
		for (trans t : LocalizedElements) {
			locList.put(t.getId(), t.getData());
		}
		
		for (trans element: EnglishElements) {	// Mark string which is equal to English
						
			if (element.getData().compareTo(locList.get(element.getId()).toString()) == 0)
				{
					System.out.print("!!! ");
					of.writeChars("!!! ");
				}
			
			System.out.println(locList.get(element.getId()));
			of.writeBytes(locList.get(element.getId()));
			of.writeUTF("\r\n");
			
		}
		
	}

	public static void startBrowser(String locale) {	// start browser with necessary locale
				
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", locale);
		driver = new FirefoxDriver(profile);
		driver.get(baseUrl);
	}
		
	static String changeLocale(int i) {	//just Enum for list of available locale
					
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
	
	static void getDataToList(List<trans> Elements) {	//getting data for current locale 
		
		List<WebElement> baseList = driver.findElements(By.xpath(".//*/a"));
				
		for (WebElement link : baseList) {
			if (link.isDisplayed() == true & !(link.getText().isEmpty()) ) 
			{
				Elements.add(new trans(link.getAttribute("h"), link.getText()));
			}
		}
	}

	static void writeToXml() {
								
					Element page = doc.createElement(locale);
					rootElement.appendChild(page);
					
					String attribute;
											
					for (trans y: LocalizedElements) {
						
						attribute = y.getId();
						attribute = attribute.replace(',', '.');
						attribute = attribute.replace('=', '.');
						
						page.setAttribute(attribute, y.getData());
						
					}
				
		}
}

class trans {	//support class to keep the connection between id and data
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
import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.JOptionPane;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriver driver;
		String baseUrl = "http://www.bing.com/";
		FirefoxProfile profile = new FirefoxProfile();
		String locale;
				 			
		locale = changeLocale();
//		locale = "ru";
		profile.setPreference("intl.accept_languages", locale);
		driver = new FirefoxDriver(profile);
		driver.get(baseUrl);
		List<WebElement> items = getItems(driver);
		
//		if (!(fileExists())) write(driver, locale, items);
//		else
//		read(driver, items);
//		write(driver, locale, items);
		
		compare(driver, locale, items);
		
		driver.close();
	}
		
	static List<WebElement> getItems(WebDriver driver) {
			
		return driver.findElements(By.xpath(".//*[@id='sc_hdu']//li"));
	} 
	
	static String[] write(WebDriver driver, String locale, List<WebElement> items) {
		
		List<WebElement> links = driver.findElements(By.xpath(".//*[@id='sc_hdu']//a"));
		String[] writeValues = new String[items.size()];
				
			try {
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		 		Document doc = docBuilder.newDocument();
				
				Element rootElement = doc.createElement(locale);
				doc.appendChild(rootElement);
							
				Element page = doc.createElement("HomePage");
				rootElement.appendChild(page);
				
				System.out.println("Write to file. Current locale is: " + locale);
										
				for (int y=0; y<items.size(); y++) {
										
					System.out.println(items.get(y).getAttribute("id") + " " + links.get(y).getAttribute("text"));
					writeValues[y] = links.get(y).getAttribute("text");
					page.setAttribute(items.get(y).getAttribute("id"), links.get(y).getAttribute("text"));
					
				}
			
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File("languages.xml"));
			 						 
					transformer.transform(source, result);
			 
					System.out.println("File saved!");
					System.out.println("----------------------------");
			
			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			  } catch (TransformerException tfe) {
				tfe.printStackTrace();
			  }
		return writeValues;	
			
	}
		
	static String[] read(WebDriver driver, List<WebElement> items) {
		
		String[] readValues = new String[items.size()];
		
		try {
			 
			File fXmlFile = new File("languages.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 			
			doc.getDocumentElement().normalize();
		 
			System.out.println("Read File. Locale in file: " + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("HomePage");
		 			 
			Node nNode = nList.item(0);
		 
			Element eElement = (Element) nNode;
			NamedNodeMap nnm = eElement.getAttributes();
									
			for (int n=0; n<nnm.getLength(); n++)			{
			
				System.out.println(items.get(n).getAttribute("id") + ": " + eElement.getAttribute(items.get(n).getAttribute("id").toString()));
				readValues[n] = eElement.getAttribute(items.get(n).getAttribute("id").toString());
			}

			System.out.println("----------------------------");
								
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		return readValues;
		
		
	}

	static boolean fileExists(){
		File f = new File("languages.xml");
		 
		  if(f.exists()){
			  System.out.println("File exists");
			  return true;
		  }
		  else{
			  System.out.println("File not found!");
			  return false;
		  }
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
	
	static boolean compare(WebDriver driver, String locale, List<WebElement> items) {
		
		String[] read = read(driver, items);
		String[] write = write(driver, locale, items);
		
		
		for (int z=0; z<read.length; z++) {
			System.out.println("read: " + read[z] + " write: " + write[z]);
			
			if (read[z].compareTo(write[z]) != 0) {
				System.out.println("Locale was changed");
				return true;
			}
				
		}
		
		System.out.println("Locale hasn't been changed");
		return false;
	}
	
	
}


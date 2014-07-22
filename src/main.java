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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
		profile.setPreference("intl.accept_languages", locale);
		driver = new FirefoxDriver(profile);
		driver.get(baseUrl);
		
		if (!(fileExists())) write(driver, locale);
		else
		read();
		write(driver, locale);
		
		driver.close();
	}
		
	static void write(WebDriver driver, String locale) {
		List<WebElement> items = driver.findElements(By.xpath(".//*[@id='sc_hdu']//li"));
		List<WebElement> links = driver.findElements(By.xpath(".//*[@id='sc_hdu']//a"));
				
			try {
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		 		Document doc = docBuilder.newDocument();
				
				Element rootElement = doc.createElement(locale);
				doc.appendChild(rootElement);
							
				Element page = doc.createElement("HomePage");
				rootElement.appendChild(page);
				
				System.out.println("locale is " + locale);
										
				for (int y=0; y<items.size(); y++) {
										
					System.out.println(items.get(y).getAttribute("id") + " " + links.get(y).getAttribute("text"));
					
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
			
	}
		
	static void read() {
		try {
			 
			File fXmlFile = new File("languages.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 			
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("HomePage");
		 			 
			Node nNode = nList.item(0);
		 
			Element eElement = (Element) nNode;
		 										
			System.out.println("scpt0 : " + eElement.getAttribute("scpt0").toString());
			System.out.println("scpt1 : " + eElement.getAttribute("scpt1").toString());
			System.out.println("scpt2 : " + eElement.getAttribute("scpt2").toString());
			System.out.println("scpt3 : " + eElement.getAttribute("scpt3").toString());
			System.out.println("scpt4 : " + eElement.getAttribute("scpt4").toString());
			System.out.println("msn : " + eElement.getAttribute("msn").toString());
			System.out.println("outlook : " + eElement.getAttribute("outlook").toString());
				
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		
		
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
}


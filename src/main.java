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




public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en");
		WebDriver driver = new FirefoxDriver(profile);
		
		String baseUrl = "http://www.bing.com/";
		driver.get(baseUrl);
		
		List<WebElement> items = driver.findElements(By.xpath(".//*[@id='sc_hdu']//a")); 
		
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			String locale = "English";
			Element rootElement = doc.createElement(locale);
			doc.appendChild(rootElement);
			
			
		for (WebElement i : items) {
			System.out.println(i.getAttribute("text"));
		}
		
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("languages.xml"));
		 
						 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
		
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		
	}

}

package nbpapi;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ParserTest {

	@Test
	public void testGoldPriceParser() throws SAXException, FileNotFoundException, IOException {
		goldParser gparser = new goldParser();
		Float f1 = new Float(0);

		f1 = gparser.goldPriceParser("http://api.nbp.pl/api/cenyzlota/2017-12-29/?format=xml");

		Float f2 = new Float(145.48); //expected result
		assertEquals(f1, f2);
	}
	@Test
	public void testGoldAvgParser() throws SAXException, FileNotFoundException, IOException {
		goldParser gparser = new goldParser();
		Float f1 = new Float(0);
		
		f1 = gparser.goldAvgParser("http://api.nbp.pl/api/cenyzlota/2017-01-01/2017-01-31/?format=xml");

		Float f2 = new Float(157.53); //expected result
		assertEquals(f1, f2);
	}
	@Test
	public void testGetMyCurrency() throws SAXException, FileNotFoundException, IOException, ParserConfigurationException {
		currenciesParser cparser = new currenciesParser();
		String s1 = "";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	
		DocumentBuilder db;
		Document doc = null; 
  		XPath xpath = XPathFactory.newInstance().newXPath();	
	
		db = dbf.newDocumentBuilder();
		doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/2017-12-29/?format=xml").openStream());
		s1 = cparser.getMyCurrency(doc, xpath, "USD");
		
		String s2 = "3.4813"; //expected result
		assertEquals(s1, s2);
	}
	@Test
	public void testGetTheBiggestAmplitude() throws SAXException, FileNotFoundException, IOException, ParserConfigurationException {
		currenciesParser cparser = new currenciesParser();
		String s1 = "";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	
		DocumentBuilder db;
		Document doc = null; 
  		XPath xpath = XPathFactory.newInstance().newXPath();	
	
		db = dbf.newDocumentBuilder();
		doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/2017-01-01/2017-02-28/?format=xml").openStream());
		s1 = cparser.getTheBiggestAmplitude(doc, xpath);
		
		String s2 = "USD - 0.24359989"; //expected result
		assertEquals(s1, s2);
	}

}

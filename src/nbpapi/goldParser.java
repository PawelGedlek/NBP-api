package nbpapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class goldParser {	
	/*
	 * 
	 * method which parse price of gold 
	 * in given day
	 * 
	 */
	public float goldPriceParser(String url) throws MalformedURLException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 		
		float price = 0;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			
			NodeList goldList = doc.getElementsByTagName("CenaZlota");
			for(int i=0; i<goldList.getLength(); i++){
				Node c = goldList.item(i);
				if(c.getNodeType()==Node.ELEMENT_NODE){
					Element gd = (Element) c;
					NodeList fieldList = gd.getChildNodes();
					for(int j=0; j<fieldList.getLength(); j++){
						Node f = fieldList.item(j);
						if(f.getNodeType()==Node.ELEMENT_NODE){
							Element field = (Element) f;
							if(field.getTagName().equals("Cena")){
								price = Float.parseFloat(field.getTextContent());			
							}
						}
					}
				}
			}
			
		}
		catch(ParserConfigurationException e){
			System.out.println("ParserConfigurationException occurs");
		} catch (SAXException e) {
			System.out.println("SAXException occurs");
		} catch (IOException e) {
			System.out.println("IOException occurs");
		}
		
		return price;
		}
	/*
	 * 
	 * method which parse average price of gold 
	 * in given period of time
	 * 
	 */
	public float goldAvgParser(String url) throws MalformedURLException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 		float m=0; int days=0;
		try{
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			
			
			NodeList goldList = doc.getElementsByTagName("CenaZlota");
			for(int i=0; i<goldList.getLength(); i++){
				Node c = goldList.item(i);
				if(c.getNodeType()==Node.ELEMENT_NODE){
					Element gd = (Element) c;
					NodeList fieldList = gd.getChildNodes();
					for(int j=0; j<fieldList.getLength(); j++){
						Node f = fieldList.item(j);
						if(f.getNodeType()==Node.ELEMENT_NODE){
							Element field = (Element) f;
							if(field.getTagName().equals("Cena")){
								m += Float.parseFloat(field.getTextContent());			
								days++;
							}
						}
					}
				}
			}
			
		}
		catch(ParserConfigurationException e){
			System.out.println("ParserConfigurationException occurs");
		} catch (SAXException e) {
			System.out.println("SAXException occurs");
		} catch (IOException e) {
			System.out.println("IOException occurs");
		}
			return m/days;
		}
}

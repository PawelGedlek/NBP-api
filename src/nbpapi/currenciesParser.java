package nbpapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class currenciesParser {
		/*
		 * 
		 * method which parse price of 
		 * given currency 
		 * 
		 */
		public static String getMyCurrency(Document doc, XPath xpath, String code){  
		  // XPath Query 
			String myCurrency = "";
			XPathExpression expr;
			
			Object result = null;
			try {
				expr = xpath.compile("//Rates/Rate//text()");
				result = expr.evaluate(doc, XPathConstants.NODESET);
			} 
			
			catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {	
				if(nodes.item(i).getNodeValue().equals(code))
					myCurrency =  nodes.item(i+1).getNodeValue(); 
			}
			return myCurrency;
	  }
		/*
		 * 
		 * method which parse the cheapest
		 * currency in given day
		 * 
		 */
		public static String getTheCheapestCurrency(Document doc, XPath xpath){  
		  // XPath Query 
		  	float min = 10; String searchingCurrency = "";
			XPathExpression expr;
			
			Object result = null;
			try {
				expr = xpath.compile("//Rates/Rate//text()");
				result = expr.evaluate(doc, XPathConstants.NODESET);
			} 
			
			catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			NodeList nodes = (NodeList) result;

			for (int i = 0; i < nodes.getLength(); i++) {	
				if(nodes.item(i).getParentNode().getNodeName().equals("Bid")){
					float tmp = Float.parseFloat(nodes.item(i).getNodeValue());
					if(tmp < min){
						min = tmp;
						searchingCurrency = nodes.item(i-2).getNodeValue() + " - " + nodes.item(i).getNodeValue() ;
					}
				}
					
			}
			return searchingCurrency;
	  }
		/*
		 * 
		 * method which parse sorted list of
		 * currencies from the lowest to the highest
		 * difference between bid and ask
		 * 
		 */
		public static List getSortedCurrenies(Document doc, XPath xpath){  
		    // XPath Query 
		    List <Currency> currList = new ArrayList<Currency>();

			XPathExpression expr;
			
			Object result = null;
			try {
				expr = xpath.compile("//Rates/Rate//text()");
				result = expr.evaluate(doc, XPathConstants.NODESET);
			} 
			
			catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			NodeList nodes = (NodeList) result;
			/*
			 * 
			 * sort my currencies
			 * 
			 */
			float bid = 0, ask = 0;
			for (int i = 0; i < nodes.getLength(); i++) {	

				if(nodes.item(i).getParentNode().getNodeName().equals("Bid")){
					bid = Float.parseFloat(nodes.item(i).getNodeValue());
				}
				else if(nodes.item(i).getParentNode().getNodeName().equals("Ask")){
					ask = Float.parseFloat(nodes.item(i).getNodeValue());
					currList.add(new Currency(nodes.item(i-2).getNodeValue(), ask-bid));
					bid = 0; ask = 0;
				}
			}
			
			Collections.sort(currList);
			return currList;
	  }
		
		/*
		 * 
		 * method which parse the biggest
		 * amplitude between price of currency
		 * in given period of time
		 * 
		 */
		public static String getTheBiggestAmplitude(Document doc, XPath xpath){  
		  // XPath Query 
	        Map<String, Amplitude> currencies = new HashMap<>();
			
			XPathExpression expr;
			
			Object result = null;
			try {
				expr = xpath.compile("//Rates/Rate//text()");
				result = expr.evaluate(doc, XPathConstants.NODESET);
			} 
			
			catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			NodeList nodes = (NodeList) result;

			for (int i = 0; i < nodes.getLength(); i++) {	
				if(nodes.item(i).getParentNode().getNodeName().equals("Code")){
					Amplitude amp = new Amplitude( nodes.item(i).getNodeValue(), 10, 0);
					currencies.put(nodes.item(i).getNodeValue(), amp);
				}
					
			}
			
			for (int i = 0; i < nodes.getLength(); i++) {	
				if(nodes.item(i).getParentNode().getNodeName().equals("Mid")){
					float tmp = Float.parseFloat(nodes.item(i).getNodeValue());
					String currentCurrency = nodes.item(i-1).getNodeValue();

					if(currencies.get(currentCurrency).getMaxValue() < tmp){
						currencies.get(currentCurrency).setMaxValue(tmp);
					}
					if(currencies.get(currentCurrency).getMinValue() > tmp){
						currencies.get(currentCurrency).setMinValue(tmp);
					}
				}
					
			}
	         
	        Set<Entry<String,Amplitude>> entrySet = currencies.entrySet();
			String mresult = ""; 
			float diff, maxDiff = 0;
	        for(Entry<String, Amplitude> entry: entrySet) {
	            diff = entry.getValue().getMaxValue()-entry.getValue().getMinValue();
	            if(maxDiff < diff){
	            	maxDiff = diff;
	            	mresult = entry.getKey().toString();
	            }
	        }
			
			return  mresult + " - " + maxDiff;
	  }
		

		/*
		 * 
		 * method which parse the lowest and the highest
		 * price of currency in given period of time
		 * 
		 */
		
		public static Pair getCurrencyBounds(Document doc, XPath xpath, String code){  
			  // XPath Query 
				String myCurrency = "";
				XPathExpression expr;
				float min = 10, max = 0;
				Object result = null;
				try {
					expr = xpath.compile("//Rates/Rate//text()");
					result = expr.evaluate(doc, XPathConstants.NODESET);
				} 
				
				catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				
				NodeList nodes = (NodeList) result;
				for (int i = 0; i < nodes.getLength(); i++) {	
					if(nodes.item(i).getNodeValue().equals(code)){
						float tmp = Float.parseFloat(nodes.item(i+1).getNodeValue());
						if(min>tmp) min = tmp;
						if(max<tmp) max = tmp;
					}
				}
				Pair pair = new Pair(max, min);
				
				return pair;
		  }
		/*
		 * 
		 * method which parse list of courses 
		 * given Currency in given period of time
		 * 
		 */
		public static List getCoursesList(Document doc, XPath xpath, String code){  
			  // XPath Query
				List<Currency> currList = new <Currency>ArrayList();
				String myCurrency = "";
				XPathExpression expr;
				
				Object result = null;
				try {
					expr = xpath.compile("//Rates/Rate//text()");
					result = expr.evaluate(doc, XPathConstants.NODESET);
				} 
				catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				
				NodeList nodes = (NodeList) result;
				for (int i = 0; i < nodes.getLength(); i++) {	
					if(nodes.item(i).getNodeValue().equals(code)){
						float tmp = Float.parseFloat(nodes.item(i+1).getNodeValue());
						Currency c = new Currency(code, tmp);
						currList.add(c);
					}
				}
				return currList;
		  }
}

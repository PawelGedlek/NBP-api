package nbpapi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
public class Main {
	public static void main(String[] args) throws MalformedURLException, SAXException, IOException, ParseException {
  		if(args.length < 3){
  			System.out.println("Prawid³owe wykonanie programu w postaci:\njava -jar nazwaArchiwum.jar (param1: yyyy-mm-dd) (param2: yyyy-mm-dd) (param3 currency)");
  			System.out.println("Obs³ugiwane przez program opcje to:\n");
  			System.out.println("1) Kurs z³ota w podanym dniu,\n" + "2) Kurs twojej waluty w podanym dniu,\n"+"3) Œrednia cena z³ota za podany okres,\n"+
  			"4) Waluta o najwiekszej amplitudzie wahañ,\n"+"5) Najtañsza waluta w podanym dniu,\n"+"6) N walut posortowanych wzgledem roznicy cen kupna i sprzeda¿y,\n"+
  			"7) Najwy¿sza i najni¿sza cena podanej waluty,\n"+"8) Lista kursów walut,");
  		}
		
  		else{
  			String date1 = args[0];
  			String date2 = args[1];
  			String currencyName = args[2];
  			
  			currenciesParser cparser = new currenciesParser();
  			goldParser gparser = new goldParser();
  			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	
  			DocumentBuilder db;
  			Document doc = null; 
  	  		XPath xpath = XPathFactory.newInstance().newXPath();
		try{
			//check if date1 before date2
			if(checkDates(date1, date2)){
				int nod = (int)numberOfDays(date1, date2);
				//price of gold in given date
				System.out.println("Cena zlota z dnia " + date1 + ": " + gparser.goldPriceParser("http://api.nbp.pl/api/cenyzlota/" + date1+ "/?format=xml")+"\n");
			
				//price of currency in given date
				db = dbf.newDocumentBuilder();
				doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/a/" + date1 + "/?format=xml").openStream());
				System.out.println("Kurs " + currencyName +" z dnia: " + date1 +": "+ cparser.getMyCurrency(doc, xpath, currencyName) +" PLN\n");
				
				//average price of currency in given period of time
				if(nod < 367){
					System.out.println("Œrednia cena zlota w okresie od "+date1+" do "+date2+": "+ 
					gparser.goldAvgParser("http://api.nbp.pl/api/cenyzlota/"+date1+"/"+date2+"/?format=xml")+"\n");
				}
				else{
					double iter = (float)nod/367;
					float result = 0; int it=1;
					String newStart = date1, newEnd = ""; 
					for(double i=0; i<Math.ceil(iter)-1; i++){
						newEnd = getNewYear(newStart);
						result += gparser.goldAvgParser("http://api.nbp.pl/api/cenyzlota/"+newStart+"/"+newEnd+"/?format=xml"+"\n");
						newStart = getNewStart(newEnd);
						it++;
					}
		
					result += gparser.goldAvgParser("http://api.nbp.pl/api/cenyzlota/"+newStart+"/"+date2+"/?format=xml");
					System.out.println("Œrednia cena zlota w okresie od "+newStart+" do "+date2+": "+ result/it+"\n");
				}
				
				//the biggest amplitude in given period of time
				db = dbf.newDocumentBuilder();
				if(nod < 93){
					doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+date1+ "/"+date2 +"/?format=xml").openStream());
					System.out.println("Najwy¿sze wahania dozna³ w okresie od "+date1+" do "+date2+": "+ cparser.getTheBiggestAmplitude(doc, xpath)+"\n");
				}
				else{
					String newEnd = getNewEnd(date1);
					doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+date1+ "/"+newEnd +"/?format=xml").openStream());
					System.out.println("Najwy¿sze wahania dozna³ w okresie od "+date1+" do "+date2+": "+ cparser.getTheBiggestAmplitude(doc, xpath)+"\n");
				}
					
				
				//the cheapest currency in given day
				db = dbf.newDocumentBuilder();
				doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/C/"+ date1+"/?format=xml").openStream());
				System.out.println("Najtañsza waluta w dniu "+date1 + ": "+ cparser.getTheCheapestCurrency(doc, xpath) + " PLN\n");
			
				//list of sorted currencies according to difference between price of bid and price of ask
				db = dbf.newDocumentBuilder();
				doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/C/"+ date1 +"/?format=xml").openStream());
				System.out.println("Lista walut posortowanych wg róznicy miedzy cena kupna i sprzeda¿y z dnia "+date1+": \n"+ 
				cparser.getSortedCurrenies(doc, xpath));
				
				//show the lowest and the highest price of currency in given period of time
				db = dbf.newDocumentBuilder();
				if(nod < 93){
					doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+date1+"/"+date2+"/?format=xml").openStream());
					Pair p1 = cparser.getCurrencyBounds(doc, xpath, currencyName);
					System.out.println("\nKursy "+ currencyName + " z okresu od " + date1 + " do "+ date2 + ":\n"+
					"Najwyzszy kurs: "+ p1.getNewMax() +" PLN\nNajnizszy kurs: " + p1.getNewMin() +" PLN\n");
				}
				else{
					double iter = (float)nod/93;
					String newStart = date1, newEnd = ""; 
					float myMax = 0, myMin = 100;
					for(double i=0; i<Math.ceil(iter)-1; i++){
						newEnd = getNewEnd(newStart);
						doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+newStart+"/"+newEnd+"/?format=xml").openStream());
						Pair p1 = cparser.getCurrencyBounds(doc, xpath, currencyName);
						if(myMax < p1.getNewMax()) myMax = p1.getNewMax(); 
						if(myMin > p1.getNewMin()) myMin = p1.getNewMin(); 
						newStart = getNewStart(newEnd);
					}
					doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+newStart+"/"+date2+"/?format=xml").openStream());
					Pair p1 = cparser.getCurrencyBounds(doc, xpath, currencyName);
					if(myMax < p1.getNewMax()) myMax = p1.getNewMax(); 
					if(myMin > p1.getNewMin()) myMin = p1.getNewMin(); 
					System.out.println("\nKursy "+ currencyName + " z okresu od " + newStart + " do "+ date2 + ":\n"+
					"Najwyzszy kurs: "+myMax +" PLN\nNajnizszy kurs: " + myMin+" PLN\n");
				}	
				
				//draw charts with given currency
				db = dbf.newDocumentBuilder();
				List<Currency> listOfCourses = new <Currency>ArrayList();

				if(nod < 93){
					doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+ date1 +"/" + date2 +"/?format=xml").openStream());	
					listOfCourses.addAll(cparser.getCoursesList(doc, xpath, currencyName));
				}
				else{
					double iter = (float)nod/93;
					String newStart = date1, newEnd = ""; 
						
					for(double i=0; i<Math.ceil(iter)-1; i++){
						newEnd = getNewEnd(newStart);
						doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+ newStart +"/" + newEnd +"/?format=xml").openStream());
						listOfCourses.addAll( cparser.getCoursesList(doc, xpath, currencyName) );
						newStart = getNewStart(newEnd);
					}
					doc = db.parse(new URL("http://api.nbp.pl/api/exchangerates/tables/A/"+ newStart +"/" + date2 +"/?format=xml").openStream());
					listOfCourses.addAll(cparser.getCoursesList(doc, xpath, currencyName));
				}
				Date d1 = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				try{
					d1 = sdf.parse(date1);
				}
				catch(ParseException e){
					System.out.println("Date Parse Exception");
				}
				
				Weeks weeks = new Weeks();
				System.out.println(weeks.drawCharts(listOfCourses, d1));
				
			}
			else
				System.err.println("Wrong period of time!");
		}
		catch (FileNotFoundException e){
			System.err.println("File does not exist");
		}
		catch (IOException e) {
		     System.err.println("IOException occurs");
		}
		catch (ParserConfigurationException e) {
			System.err.println("ParserConfigurationException occurs");
		}
		catch (SAXException e) {
			System.err.println("SAXException occurs");
		}		
  		}
	}
	public static boolean checkDates(String s1, String s2){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	      try {
	         Date d1 = sdf.parse(s1);
	         Date d2 = sdf.parse(s2);
	         if(d1.after(d2))
	        	 return false;
	      } catch (ParseException e) { 
	         System.err.println("Date Parse Exception");
	      }
	 
		return true;
	}
	public static String getMyDate(){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  Scanner sc = new Scanner(System.in);
	      System.out.print("Podaj date w formacie yyyy-MM-dd: ");
	      String str = sc.nextLine();	 
	      
	      try {
	         Date date = sdf.parse(str); 
	      } catch (ParseException e) { 
	         System.out.println("Date Parse Exception");
	      }
	      
		return str;
	}
	public static String getCurrency(){
		
		System.out.print("Podaj szukana walute: ");
		  Scanner sc = new Scanner(System.in);
	      String str = sc.nextLine();	 
	      
		return str;
	}
	public static long numberOfDays(String s1, String s2){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long sum = 0;
		
		try{
			Date d1 = sdf.parse(s2);
			Date d2 = sdf.parse(s1);
			long div = 86400000; // numbers of miliseconds per day
			sum = (d1.getTime() - d2.getTime())/div;
		}
		catch(ParseException e){
			System.out.println("Date Parse Exception");
		}

		return sum;
	}
	public static String getNewEnd(String s1){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String result = "";
		try{
			Date d1 = sdf.parse(s1);
			for(int i=0;i<23;i++)
				d1.setTime(d1.getTime()+(4*86400000));
			result = sdf.format(d1);
		}
		catch(ParseException e){
			System.out.println("Date Parse Exception");
		}
		return result;
	}
	public static String getNewStart(String s1){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String result = "";
		try{
			Date d1 = sdf.parse(s1);
			d1.setTime(d1.getTime()+86400000);
			result = sdf.format(d1);
		}
		catch(ParseException e){
			System.out.println("Date Parse Exception");
		}
		return result;
	}
	public static String getNewYear(String s1){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String result = "";
		try{
			Date d1 = sdf.parse(s1);
			for(int i=0;i<61;i++)
				d1.setTime(d1.getTime()+(6*86400000));
			result = sdf.format(d1);
		}
		catch(ParseException e){
			System.out.println("Date Parse Exception");
		}
		return result;
	}
}

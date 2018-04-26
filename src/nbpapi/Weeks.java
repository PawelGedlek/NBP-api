package nbpapi;

import java.util.Date;
import java.util.List;

public class Weeks {
	
	public String drawCharts(List<Currency> currList, Date d1){
		StringBuilder builder = new StringBuilder();
		
		DaysOfTheWeek dtw = DaysOfTheWeek.Friday; 
		DaysOfTheWeek dotw = dtw.getDaysOfTheWeek(d1);
		
		float div = currList.get(0).getValue()/10;
		
		int numberOfWeek = 1;
		builder.append( "Currencies charts:\n---------------------------------------------------------------------------------------------\n");
		builder.append( "| " + currList.get(0).getName() +"\n");
		
		if(!dotw.equals(DaysOfTheWeek.Monday)){
			builder.append("---------------------------------------------------------------------------------------------\n" +
					"Week: "+ numberOfWeek +"\n---------------------------------------------------------------------------------------------\n");  
			numberOfWeek++; 
		}
		for(Currency c : currList){
			if(dotw.equals(DaysOfTheWeek.Monday)){
				builder.append("---------------------------------------------------------------------------------------------\n" +
				"Week: "+ numberOfWeek +"\n---------------------------------------------------------------------------------------------\n");  
				numberOfWeek++;
			}
			builder.append(dotw +"  \t| " + c.getValue() + " \t| "+ getCharts(c.getValue(), div) + "\n");
			dotw = dotw.nextDay();
			
		}
		
		
		return builder.toString();
	}
	
	public String getCharts(float course, float inc){
		String result = "";
		
		for(float i = 0; i < course/inc; i+=0.1){
			result +=  '*';
		}
		
		return result;
	}
}

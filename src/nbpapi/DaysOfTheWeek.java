package nbpapi;

import java.util.Date;

public enum DaysOfTheWeek {
	Monday, Tuesday, Wednesday, Thursday, Friday;
	
	public DaysOfTheWeek getDaysOfTheWeek(Date d){
		DaysOfTheWeek result = Friday;
		switch(d.getDay()){
			case 1: result = Monday; break;
			case 2: result = Tuesday; break;
			case 3: result = Wednesday; break;
			case 4: result = Thursday; break;
			default: break;
		}
		return result;
	}
	
	public DaysOfTheWeek nextDay(){
		return this.ordinal() < DaysOfTheWeek.values().length - 1
			? DaysOfTheWeek.values()[this.ordinal() + 1]
		    : DaysOfTheWeek.values()[0];
	}
}

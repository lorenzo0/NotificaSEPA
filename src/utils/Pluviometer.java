package utils;

import java.util.HashMap;

public class Pluviometer {
	
	private String observation;
	private double value;
	private String timeStamp;
	private double oldValue;
	private double hourValue;
	
	
	public Pluviometer(String observation, double value, String timeStamp) {
		this.observation = observation;
		this.value = value;
		this.timeStamp = timeStamp;
		oldValue = 0;
	}
	
	public int getNewWaterValue() {
		if(oldValue!=0) {
			hourValue = value + oldValue;
			this.oldValue = value;
		}else return -1;
		
		if(hourValue<2)	return 0;
		else if(hourValue>=2 && hourValue<4) return 1;
		else if(hourValue>=4 && hourValue<6) return 2;
		else if(hourValue>=6 && hourValue<10) return 3;
		else if(hourValue>=10 && hourValue<30) return 4;
		else if(hourValue>=30) return 5;
		
		return 0;
	}
	
	public void updateValues(double newValue, String newTimeStamp) {
		this.value = newValue;
		this.timeStamp = newTimeStamp;
	}

	public String getObservation() {
		return observation;
	}


	public void setObservation(String observation) {
		this.observation = observation;
	}


	@Override
	public String toString() {
		return "Pluviometer [observation=" + observation + ", value=" + value + ", timeStamp=" + timeStamp + "]";
	}	

}

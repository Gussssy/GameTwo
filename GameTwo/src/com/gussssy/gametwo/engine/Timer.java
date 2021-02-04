package com.gussssy.gametwo.engine;

/**
 * Used to record processing time for blocks of code / processes.   
 **/
public class Timer {
	
	long loggedTime;
	long totalTime;
	long averageTime;
	long duration;
	long totalTimeThisPrint;
	long averageTimeThisPrint;
	long largestThisPrint;
	long largestTime = 0;
	int counter;
	private boolean print = true;
	int updatesPerPrint = 60;
	String processName;
	
	public Timer(String processName){
		this.processName = processName;
		
	}
	
	/**
	 * Logs the Start Time. 
	 */
	public void startTime(){
		loggedTime = System.nanoTime();
	}
	
	
	/**
	 * Logs the End Time.
	 *
	 */
	public void endTime(){
		counter++;
		duration = System.nanoTime() - loggedTime;
		totalTime += duration;
		totalTimeThisPrint += duration;
		
		if(duration > largestTime){
			//System.out.println("!!!!!! " + processName + " experienced its worst time yet:" + duration/1000000);
			largestTime = duration;
			if(counter < 360)largestTime = 0;
		}
		
		if(duration/1000000 > 16.7){
			//System.out.println("!!! " + processName + " time exceeded 1/60th of a second. Time: " + duration/1000000);
			
		}
		
		if(duration > largestThisPrint)largestThisPrint = duration;
		
		//System.out.println(processName + ": " + duration);
		
		
		if(print && counter%updatesPerPrint == 0){
			averageTime = totalTime / counter;
			System.out.println(processName + " has taken on average: " + averageTime + "ms over " + counter + " iterations");
			//averageTimeThisPrint = totalTimeThisPrint/updatesPerPrint;
			//System.out.println(processName + " over the last " + updatesPerPrint + " has taken on average: " + averageTimeThisPrint + "ms");
			//System.out.println("\t\t largest time: " + largestThisPrint + "ms");
			totalTimeThisPrint = 0;
			largestThisPrint = 0;
		}
		
		
	}
	

}

package com.gussssy.gametwo.game.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class reponsible for storing errors messages in a text file.  
 **/
public class Log {
	
	// holds the number of updates
	int update = 0; 
	
	// the log file
	File logFile;
	
	// log file, file writer
	FileWriter writer;
	
	// Messages to added to the log
	ArrayList<String> logEntries = new ArrayList<String>();
	
	
	
	/**
	 * LogWriter Constructor
	 **/
	public Log(){
		
		logFile = new File("log.txt");
		
		// detele the old log file
		logFile.delete();
		
		// make the new log file
		try {
			if(logFile.createNewFile()){
				System.out.println("Created Log File");
			}else {
				System.out.println("Did not create Log File, file with same name already exists");
			}
		} catch (IOException e) {
			System.out.println("Something went wrong creating the log file");
			e.printStackTrace();
		}
		
		
		try {
			writer = new FileWriter(logFile);
			writer.write("Start Log: " + System.currentTimeMillis());
		} catch (IOException e) {
			System.out.println("Something went wrong creating log file writer");
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	/**
	 * Writes any log entries received since last update to file  
	 **/
	public void update(){
		
		update++;
		//System.out.println("log update: " + update);
		
		// write logs
		try {
			//writer.write("\nlog update: " + update);
			for(String logEntry : logEntries){
				writer.write(logEntry);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		logEntries.clear();
		
		
	}
	
	
	public void log(String logEntry){
		
		logEntries.add(logEntry);
		
	}
	

}

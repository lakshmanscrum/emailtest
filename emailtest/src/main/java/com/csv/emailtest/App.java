package com.csv.emailtest;

import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;

/**
 * This class will read the emails from csv and send to the users 
 *
 */
public class App 
{
	public static final  String CSV_FILE_PATH="C://lakshman//eclipse//workspace//emailtest//csv//emaillist.csv";
	public static final  char CSV_SEPERATOR=';';
	public static final  char DOUBLE_QUOTES='"';
	// Thread pool size
	public static final  char POOL_SIZE=10;
	final static Logger errorLog = Logger.getLogger("admin");
	final static Logger logFile = Logger.getLogger("file");


	   

	/**
	 * This method will read the email, firstname and last name from csv file
	 *  
	 */
	public void readCsv()  {
		logFile.debug("Started readCsv() "  );
		CSVReader reader = null;
		SendEmail sendEmail ;
		ExecutorService executorService = null;
		Future<String> future = null;
		try {
			
            reader = new CSVReader(new FileReader(CSV_FILE_PATH), ';');
            String[] line;
             executorService = Executors.newFixedThreadPool(POOL_SIZE);
             
            while ((line = reader.readNext()) != null) {
                if(line.length == 3) {
                	sendEmail = new SendEmail(line[0], line[1], line[2]);
                	
                	future = executorService.submit(sendEmail);
                	logFile.debug("Fron CSV file Email = " + line[0] + ", first Name= " + line[1] + " , last Name=" + line[2] );

                } else {
                	errorLog.error("This is invalid input for email:" +String.join(",", line));
                }
                
            }
            while(!future.isDone()) {
        		
        	}
            
        } catch (Exception e) {
            e.printStackTrace();
            errorLog.error(e.getMessage());
        }finally {
            try {
                if (reader!=null) {
                	reader.close();
                }
                if(executorService != null) {
                	executorService.shutdown();
                }
            } catch (Exception e){
                e.printStackTrace();
                errorLog.error(e.getMessage());
            }
        }
		
		
	}
    public static void main( String[] args )
    {
        App app = new App();
        app.readCsv();
    }
}

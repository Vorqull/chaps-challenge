package Persistence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {

  /**
   * @param filePath filepath of csv file.
   */
  public ArrayList<ArrayList<String>> readCSV(String filePath) {
	ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>();
    try {
     	//Read CSV file
		BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
		//The current line of the CSV file being read
	    String currentData;
	    //The current line index (y position)
	    while((currentData = buffRead.readLine()) != null) {
	    	ArrayList<String> currentLineList = new ArrayList<String>();
	    	String[] data = currentData.split(",");

	    	for(String entry : data) {
	    		currentLineList.add(entry);
	    	}
	    	returnList.add(currentLineList);
	    }
	    buffRead.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return returnList;
  }
}

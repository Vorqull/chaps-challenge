package Persistence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Read a CSV file into a 2d ArrayList of strings.
 * @author Lukas Stanley
 *
 */
public class CsvReader {

  /**
   * Read the CSV at location filePath and turn it into an ArrayList.
   * 
   * @param filePath filepath of csv file.
   * @return the csv file in a 2d ArrayList format.
   */
  public ArrayList<ArrayList<String>> readCsv(String filePath) {
    ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>();
    try {
      // Read CSV file
      BufferedReader buffRead = new BufferedReader(new FileReader(filePath));
      // The current line of the CSV file being read
      String currentData;
      // The current line index (y position)
      while ((currentData = buffRead.readLine()) != null) {
        ArrayList<String> currentLineList = new ArrayList<String>();
        String[] data = currentData.split(",");

        for (String entry : data) {
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

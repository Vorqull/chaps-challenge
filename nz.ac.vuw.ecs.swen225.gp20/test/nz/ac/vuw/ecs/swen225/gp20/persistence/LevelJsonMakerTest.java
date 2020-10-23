package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Maze.BoardObjects.Tiles.FreeTile;
import Maze.BoardObjects.Tiles.Key;
import Persistence.Level;
import Persistence.LevelJsonMaker;
import Persistence.LevelJsonReader;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonObject;
import org.junit.Test;



/**
 * Junit test cases for LevelJsonMaker.
 * 
 * @author Lukas Stanley
 *
 */
public class LevelJsonMakerTest {

  
  /**
   * Tests that FreeTile can be written to JSON and loaded back in.
   * @throws Exception - In case of formatting issues;
   */
  @Test
  public void testMakeJson1() throws Exception {
    String[][] testArray = {
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Turn the array into a JSON file, then load the JSON file into a level.
    Level testResult = makeJsonUtility(testArray);
    assertEquals(FreeTile.class, testResult.getTileArray()[0][0].getClass());
  }
  
  /**
   * Tests that FreeTile can be written to JSON and loaded back in.
   * @throws Exception - In case of formatting issues;
   */
  @Test
  public void testMakeJson2() throws Exception {
    String[][] testArray = {
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Turn the array into a JSON file, then load the JSON file into a level.
    Level testResult = makeJsonUtility(testArray);
    assertEquals(FreeTile.class, testResult.getTileArray()[2][2].getClass());
  }
  
  /**
   * Tests that blank spaces can be written to JSON and loaded back in.
   * @throws Exception - In case of formatting issues;
   */
  @Test
  public void testMakeJson3() throws Exception {
    String[][] testArray = {
        {"", "f", "f"}, 
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Turn the array into a JSON file, then load the JSON file into a level.
    Level testResult = makeJsonUtility(testArray);
    assertEquals(null, testResult.getTileArray()[0][0]);
  }
  
  /**
   * Tests that coloured keys can be written to JSON and loaded back in.
   * @throws Exception - In case of formatting issues;
   */
  @Test
  public void testMakeJson4() throws Exception {
    String[][] testArray = {
        {"k 0 b", "k 0 y", "k 0 g"}, 
        {"k 1 r", "f", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Turn the array into a JSON file, then load the JSON file into a level.
    Level testResult = makeJsonUtility(testArray);
    assertEquals(Key.class, testResult.getTileArray()[0][0].getClass());
    Key keyColourTest = (Key) testResult.getTileArray()[0][0];
    assertEquals("Blue", keyColourTest.getColour());
  }
  
  /**
   * Tests that invalidly coloured keys will have a warning written to the JSON.
   * @throws Exception - In case of formatting issues;
   */
  @Test(expected = Exception.class) 
  public void testMakeJson5() throws Exception {
    String[][] testArray = {
        {"k 0 notblue", "f", "f"}, 
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Try to turn the array into a JSON file, causing an Exception to be thrown.
    makeJsonUtility(testArray);
  }
  
  /**
   * Tests that invalidly coloured keys will have a warning written to the JSON.
   * @throws Exception - In case of formatting issues;
   */
  @Test(expected = Exception.class) 
  public void testMakeJson6() throws Exception {
    String[][] testArray = {
        {"k 3 b", "f", "f"}, 
        {"f", "f", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Try to turn the array into a JSON file, causing an Exception to be thrown.
    makeJsonUtility(testArray);
  }
  
  /**
   * Tests that the player's starting position can be written to JSON and loaded back in.
   * @throws Exception - In case of formatting issues;
   */
  @Test
  public void testMakeJson7() throws Exception {
    String[][] testArray = {
        {"f", "f", "f"}, 
        {"f", "p", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Turn the array into a JSON file, then load the JSON file into a level.
    Level testResult = makeJsonUtility(testArray);
    assertEquals(FreeTile.class, testResult.getTileArray()[1][1].getClass());
    assertEquals(1, testResult.getPlayer().getStartingPos().getX());
    assertEquals(1, testResult.getPlayer().getStartingPos().getY());
  }
  
  /**
   * Tests that the enemy starting positions can be written to JSON,
   *  and will be recognized as otherwise freetile..
   * @throws Exception - In case of formatting issues;
   */
  @Test
  public void testMakeJson8() throws Exception {
    String[][] testArray = {
        {"f", "f", "f"}, 
        {"f", "ene", "f"}, 
        {"f", "f", "f"}, 
    };
    
    //Turn the array into a JSON file, then load the JSON file into a level.
    Level testResult = makeJsonUtility(testArray);
    assertEquals(FreeTile.class, testResult.getTileArray()[1][1].getClass());
  }
  

  /**
   * Tests a single line function, PrettyPrint.
   */
  @Test
  public void testPrettyPrint() {
    JsonObject levelInfo = Json.createObjectBuilder()
        .add("Test field 1", 1)
        .add("Test field 2", 2)
        .add("Test field 3", 3)
        .build();
    String prettyPrinted = LevelJsonMaker.prettyPrint(levelInfo);
    String expectedOutput = 
        "\n{\n" 
        + "    \"Test field 1\": 1,\n" 
        + "    \"Test field 2\": 2,\n"  
        + "    \"Test field 3\": 3\n"  
        + "}";
    assertEquals(expectedOutput, prettyPrinted);
  }
  
  
  /**
   * utility function for mass calling the main function of LevelJsonMaker.
   * Turns a 2d Array of strings (as they would be when converted from CSV) into a JSON file.
   * Then loads said JSON file into a Level object, and returns the Level object.
   * @param testArray 2d ArrayList of strings, representing a processed CSV. 
   * @return the level as Loaded by LevelJsonReader.
   * @throws Exception - In case of formatting issues;
   */
  public Level makeJsonUtility(String[][] testArray) throws Exception {
    ArrayList<ArrayList<String>> testArrayList = arrayToList(testArray);
    String testFileString = "nz.ac.vuw.ecs.swen225.gp20/"
        + "test/nz/ac/vuw/ecs/swen225/gp20/persistence/"
        + "MakeJsonTest.JSON";
    LevelJsonMaker.makeJson(testArrayList, testFileString);
    Level testLevel = LevelJsonReader.readJson(testFileString);
    return testLevel;
  }
  
  /**
   * Turns a 2d String array into a 2d ArrayList.
   * 
   * @param twoDArray - 2d String array
   * @return list, the 2d String array as a 2d ArrayList
   */
  public ArrayList<ArrayList<String>> arrayToList(String[][] twoDArray) {
    ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
    for (String[] array : twoDArray) {
      list.add(new ArrayList<String>(Arrays.asList(array)));
    }
    return list;
  }

}

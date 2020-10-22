package Persistence;

import Maze.Position;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 * Utility class for creating a level JSON file. 
 * Not used in actual game, makes level creation easier.
 * 
 * @author Lukas Stanley
 *
 */
public class LevelJsonMaker {
  
  /**
   * Makes a JSON file at the given location using a CSV file.
   * 
   * @param arrayFormat - The csv file converted to an ArrayList of ArrayLists.
   * @param jsonName    - The name of the JSON file to use.
   * @throws Exception - If any fields were incorrectly formatted and present. 
   */
  public static void makeJson(ArrayList<ArrayList<String>> arrayFormat, String jsonName) 
      throws Exception {
    int rowIndex = 0;
    int colIndex = 0;
    int playerX = 0;
    int playerY = 0;
    boolean playerSet = false;
    boolean noTile = false;

    ArrayList<Position> enemyLocations = new ArrayList<Position>();
    JsonArrayBuilder colBuilder = Json.createArrayBuilder();
    for (ArrayList<String> currentArrayList : arrayFormat) {
      colIndex = 0;
      JsonArrayBuilder rowBuilder = Json.createArrayBuilder();

      for (String s : currentArrayList) {
        noTile = false;
        String[] tileInfo = s.split(" ");
        String tileName = "ERROR!";
        String tileColour = "ERROR!";
        JsonObjectBuilder arrayObjectBuilder = Json.createObjectBuilder();
        // Set the colour of the tile if a colour was designated.
        if (tileInfo.length > 2) {
          if (tileInfo[2].equals("r")) {
            tileColour = "Red";
          } else if (tileInfo[2].equals("g")) {
            tileColour = "Green";
          } else if (tileInfo[2].equals("y")) {
            tileColour = "Yellow";
          } else if (tileInfo[2].equals("b")) {
            tileColour = "Blue";
          } else {
            throw new Exception(
              "X: " 
              + colIndex 
              + " Y: " 
              + rowIndex 
              + " \nERROR: " 
              + tileInfo[2] 
              + " is not a vaild colour"
            );
          }
        }
        // Set the tile type by name
        if (tileInfo[0].equals("k")) {
          tileName = "Key";
        } else if (tileInfo[0].equals("i")) {
          tileName = "InfoField";
        } else if (tileInfo[0].equals("l")) {
          tileName = "LockedDoor";
        } else if (tileInfo[0].equals("t")) {
          tileName = "Treasure";
        } else if (tileInfo[0].equals("w")) {
          tileName = "Wall";
        } else if (tileInfo[0].equals("f")) {
          tileName = "FreeTile";
        } else if (tileInfo[0].equals("el")) {
          tileName = "ExitLock";
        } else if (tileInfo[0].equals("ep")) {
          tileName = "ExitPortal";
          // Enemy starting point, is fundamentally a freetile
        } else if (tileInfo[0].equals("ene")) {
          tileName = "FreeTile";
          enemyLocations.add(new Position(colIndex, rowIndex));
          // Player starting point, is fundamentally a freetile
        } else if (tileInfo[0].equals("p") && playerSet == false) {
          tileName = "FreeTile";
          playerX = colIndex;
          playerY = rowIndex;
          playerSet = true;
        } else {
          noTile = true;
        }
        arrayObjectBuilder.add("column", colIndex);
        arrayObjectBuilder.add("row", rowIndex);
        arrayObjectBuilder.add("Tile Type", tileName);
        if (tileInfo.length > 1) {
          if (tileInfo[1].equals("0")) {
            arrayObjectBuilder.add("Rotation", "Horizontal");
          } else if (tileInfo[1].equals("1")) {
            arrayObjectBuilder.add("Rotation", "Vertical");
          } else {
            arrayObjectBuilder.add(
                "Rotation", "ERROR! value " 
                + tileInfo[1]
                + " used! Can only use 1 or 0 for rotation! ROW: " 
                + rowIndex 
                + " COL: " 
                + colIndex
            );
          }
        } else {
          arrayObjectBuilder.add("Rotation", "Horizontal");
        }
        if (tileInfo.length == 3) {
          arrayObjectBuilder.add("Colour", tileColour);
        }
        if (tileInfo[0].equals("i")) {
          arrayObjectBuilder.add("InfoText", "Placeholder text");
        }
        JsonObject arrayObject = arrayObjectBuilder.build();

        if (noTile == false) {
          rowBuilder.add(arrayObject);
        }
        colIndex++;
      }

      JsonObject arrayRow = Json.createObjectBuilder().add("objects", rowBuilder).build();
      colBuilder.add(arrayRow);
      rowIndex++;
    }

    // Create Json Array for enemy locations + AI

    JsonArrayBuilder enemyArrayBuilder = Json.createArrayBuilder();
    JsonObjectBuilder enemyArrayObject;
    for (int i = 0; i < enemyLocations.size(); i++) {
      enemyArrayObject = Json.createObjectBuilder();
      Position currentLoc = enemyLocations.get(i);
      enemyArrayObject.add("startingX", currentLoc.getX());
      enemyArrayObject.add("startingY", currentLoc.getY());
      enemyArrayObject.add("AI Type",
          "PLACEHOLDER, INSERT AITYPE (PatternEnemy/StalkerEnemy) v INSERT RATE OF MOVEMENT BELOW v"
      );
      enemyArrayObject.add("Tick Speed", 1);
      enemyArrayObject.add("Movement String",
          "PLACEHOLDER - USED FOR MOVEMENT OF PATTERNENEMY. FORMAT IS wasd eg: wassawdws");
      enemyArrayBuilder.add(enemyArrayObject);
    }

    JsonObjectBuilder arrayColUnbuilt = Json.createObjectBuilder();
    JsonObject arrayCol = arrayColUnbuilt.add("rows", colBuilder).build();
    
    JsonObject levelInfo = Json.createObjectBuilder()
        .add("rowCount", rowIndex)
        .add("columnCount", colIndex)
        .add("playerX", playerX)
        .add("playerY", playerY)
        .add("timeLimit", 100)
        .add("Enemies", enemyArrayBuilder.build())
        .build();
    
    JsonObject mapObject = Json.createObjectBuilder()
        .add("Tiles", arrayCol)
        .add("Level Info", levelInfo)
        .build();

    String formattedString = prettyPrint(mapObject);
    // System.out.println(formattedString);
    // Create JSOn file
    try {
      File jsonFile = new File(jsonName);
      if (jsonFile.createNewFile()) {
        System.out.println("File created: " + jsonFile.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    // Write JSON file
    try (FileWriter file = new FileWriter(jsonName)) {

      file.write(formattedString);
      file.flush();
      file.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // ALL CODE BELOW FROM
  // https://stackoverflow.com/questions/23007567/java-json-pretty-print-javax-json
  /**
   * Convert a JSON Structure into a pretty printed String.
   * 
   * @param json - a JSON structure to be pretty printed.
   * @return String - The JSON structure in pretty printed String form.
   */
  public static String prettyPrint(JsonStructure json) {
    return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
  }

  /**
   * Utility function of prettyPrint.
   * 
   * @param json - the JSON structure
   * @param options - The options being used.
   * @return The returned String.
   */
  public static String jsonFormat(JsonStructure json, String... options) {
    StringWriter stringWriter = new StringWriter();
    Map<String, Boolean> config = buildConfig(options);
    JsonWriterFactory writerFactory = Json.createWriterFactory(config);
    JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

    jsonWriter.write(json);
    jsonWriter.close();

    return stringWriter.toString();
  }

  private static Map<String, Boolean> buildConfig(String... options) {
    Map<String, Boolean> config = new HashMap<String, Boolean>();

    if (options != null) {
      for (String option : options) {
        config.put(option, true);
      }
    }

    return config;
  }
}

package Persistence;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.Position;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 * A class containing the SaveJsonMaker method, a method for creating save files.
 * 
 * @author Lukas Stanley
 *
 */
public class SaveJsonMaker {

  /**
   * A method that takes a multitude of parameters, 
   * and uses them to create a save file for a given level.
   * 
   * @param remainingTime  - The remaining time in the given level.
   * @param player         - The player as they currently are in the given level.
   * @param enemies        - The enemies as they currently are in the given level.
   * @param jsonName       - The name of the JSON file to use.
   * @param tiles          - The tiles as they are currently in the given level.
   * @return boolean       - whether or not the JSON file was successfully made.
   */
  public static boolean makeJson(
      int remainingTime,
      Player player,
      Set<AbstractActor> enemies,
      String jsonName,
      AbstractTile[][] tiles
  ) {
    JsonArrayBuilder keyArrayBuilder = Json.createArrayBuilder();
    JsonArrayBuilder treasureArrayBuilder = Json.createArrayBuilder();

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (int i = 0; i < tiles.length; i++) {
      JsonObjectBuilder arrayObjectBuilder = Json.createObjectBuilder();
      for (int j = 0; j < tiles[i].length; j++) {
        if (tiles[i][j] != null) {
          AbstractTile currentTile = tiles[i][j];
          // Set the tile as changed
          if (currentTile.isChanged()) {
            arrayObjectBuilder.add("xPos", i);
            arrayObjectBuilder.add("yPos", j);
            arrayBuilder.add(arrayObjectBuilder);

            // If the object was a key, and is currently held by the player, save it to
            // their hand
            if (currentTile instanceof Key && player.getKeys().contains(currentTile)) {
              JsonObjectBuilder keyArrayObjectBuilder = Json.createObjectBuilder();
              keyArrayObjectBuilder.add("xPos", i);
              keyArrayObjectBuilder.add("yPos", j);
              keyArrayBuilder.add(keyArrayObjectBuilder);
            } else if (currentTile instanceof Treasure) {
              JsonObjectBuilder treasureArrayObjectBuilder = Json.createObjectBuilder();
              treasureArrayObjectBuilder.add("xPos", i);
              treasureArrayObjectBuilder.add("yPos", j);
              treasureArrayBuilder.add(treasureArrayObjectBuilder);
            }
          }
        }
      }
    }

    // Create Json Array for enemy locations + AI

    JsonArrayBuilder enemyArrayBuilder = Json.createArrayBuilder();
    JsonObjectBuilder enemyArrayObject;
    for (AbstractActor enemy : enemies) {
      enemyArrayObject = Json.createObjectBuilder();
      Position currentLoc = enemy.getPos();
      Position startingLoc = enemy.getStartingPos();
      enemyArrayObject.add("startingX", startingLoc.getX());
      enemyArrayObject.add("startingY", startingLoc.getY());
      enemyArrayObject.add("currentX", currentLoc.getX());
      enemyArrayObject.add("currentY", currentLoc.getY());
      enemyArrayBuilder.add(enemyArrayObject.build());
    }
    
    JsonObject levelInfo = Json.createObjectBuilder().add("playerX", player.getPos().getX())
        .add("playerY", player.getPos().getY()).add("Keys on hand", keyArrayBuilder.build())
        .add("Treasure on hand", treasureArrayBuilder.build()).add("time remaining", remainingTime)
        .add("Enemy locations", enemyArrayBuilder.build()).build();
    JsonObject fullJsonObject = Json.createObjectBuilder().add("Changed Tiles", arrayBuilder)
        .add("Gameplay variables", levelInfo).build();

    String formattedString = prettyPrint(fullJsonObject);
    // System.out.println(formattedString);
    // Create JSOn file
    try {
      File jsonFile = new File(jsonName);
      if (jsonFile.createNewFile()) {
        jsonFile.getName();
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
      return false;
    }
    // Write JSON file
    try (FileWriter file = new FileWriter(jsonName)) {

      file.write(formattedString);
      file.flush();
      file.close();
      return true;

    } catch (IOException e) {
      return false;
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

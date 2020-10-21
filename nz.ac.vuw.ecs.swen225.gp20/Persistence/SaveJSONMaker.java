package Persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import Maze.Position;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Actors.stalker_enemy.StalkerEnemy;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.ExitLock;
import Maze.BoardObjects.Tiles.ExitPortal;
import Maze.BoardObjects.Tiles.FreeTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.LockedDoor;
import Maze.BoardObjects.Tiles.Treasure;

import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class SaveJSONMaker {

  /**
   * @param ArrayFormat - The csv file converted to an ArrayList of ArrayLists.
   * @param jsonName -  The name of the JSON file to use.
   */
  public static boolean makeJSON(int remainingTime, Player player, ArrayList<AbstractActor> enemies, String jsonName, AbstractTile[][] tiles) {
	JsonArrayBuilder keyArrayBuilder = Json.createArrayBuilder();
	JsonArrayBuilder treasureArrayBuilder = Json.createArrayBuilder();
	  
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	boolean isActivated = false;
	for(int i = 0; i < tiles.length; i++) {
		JsonObjectBuilder arrayObjectBuilder = Json.createObjectBuilder();
		for(int j = 0; j < tiles[i].length; j++) {
			isActivated = false;
			AbstractTile currentTile = tiles[i][j];
			//Set the tile as changed
			if(currentTile.isChanged()) {
				currentTile.setChangedTile();
				arrayObjectBuilder.add("xPos", i);
				arrayObjectBuilder.add("yPos", j);
				arrayBuilder.add(arrayObjectBuilder);
				
				//If the object was a key, and is currently held by the player, save it to their hand
				if(currentTile instanceof Key && player.getKeys().contains(currentTile)) {
					JsonObjectBuilder keyArrayObjectBuilder = Json.createObjectBuilder();
					keyArrayObjectBuilder.add("xPos", i);
					keyArrayObjectBuilder.add("yPos", j);
					keyArrayBuilder.add(keyArrayObjectBuilder);
				}
				else if(currentTile instanceof Treasure) {
					JsonObjectBuilder treasureArrayObjectBuilder = Json.createObjectBuilder();
					treasureArrayObjectBuilder.add("xPos", i);
					treasureArrayObjectBuilder.add("yPos", j);
					keyArrayBuilder.add(treasureArrayObjectBuilder);
				}
			}

		}
	}
	
	//Create Json Array for enemy locations + AI
	
	JsonArrayBuilder enemyArrayBuilder = Json.createArrayBuilder();
	JsonObjectBuilder enemyArrayObject;
	for(int i = 0; i < enemies.size(); i++) {
		enemyArrayObject = Json.createObjectBuilder();
		Position currentLoc = enemies.get(i).getPos();
		Position startingLoc = enemies.get(i).getStartingPos();
		enemyArrayObject.add("startingX", startingLoc.getX());
		enemyArrayObject.add("startingY", startingLoc.getY());
		enemyArrayObject.add("currentX", currentLoc.getX());
		enemyArrayObject.add("currentY", currentLoc.getY());
		enemyArrayBuilder.add(enemyArrayObject);
	}
	

	
	JsonObject levelInfo = Json.createObjectBuilder()
			.add("playerX", player.getPos().getX())
			.add("playerY", player.getPos().getY())
			.add("Keys on hand", keyArrayBuilder.build())
			.add("Treasure on hand", treasureArrayBuilder.build())
			.add("time remaining", remainingTime)
			.add("Enemy locations", enemyArrayBuilder.build())
			.build();
	JsonObject fullJSONObject = Json.createObjectBuilder()
			.add("Changed Tiles", arrayBuilder)
			.add("Gameplay variables", levelInfo)
			.build();
	
	String formattedString = prettyPrint(fullJSONObject);
	//System.out.println(formattedString);
	//Create JSOn file
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
    //Write JSON file
    try (FileWriter file = new FileWriter(jsonName)) {

        file.write(formattedString);
        file.flush();
        file.close();
        return true;

    } catch (IOException e) {
        return false;
    }
  }
  
  //ALL CODE BELOW FROM https://stackoverflow.com/questions/23007567/java-json-pretty-print-javax-json
  public static String prettyPrint(JsonStructure json) {
	    return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
	}

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

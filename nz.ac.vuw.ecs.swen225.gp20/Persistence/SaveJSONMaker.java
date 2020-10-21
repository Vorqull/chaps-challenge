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
import Maze.BoardObjects.Actors.Player;
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
  public static void makeJSON(int remainingTime, Player player, ArrayList<AbstractActor> enemies, String jsonName, AbstractTile[][] tiles) {
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	boolean isActivated = false;
	for(int i = 0; i < tiles.length; i++) {
		JsonObjectBuilder arrayObjectBuilder = Json.createObjectBuilder();
		for(int j = 0; j < tiles[i].length; j++) {
			isActivated = false;
			AbstractTile currentTile = tiles[i][j];
			if(currentTile instanceof ExitLock) {
				ExitLock castTile = (ExitLock) currentTile;
				//Truen if lock has been opened.
				isActivated = castTile.interact(new Player(new Position(0, 0)));
			}
			else if(currentTile instanceof Treasure) {
				Treasure castTile = (Treasure) currentTile;
				isActivated = castTile.isPickedUp();
			}
			else if(currentTile instanceof LockedDoor) {
				LockedDoor castTile = (LockedDoor) currentTile;
				isActivated = !castTile.isLocked();
			}
			else if(currentTile instanceof Key) {
				Key castTile = (Key) currentTile;
				//TO DO!!! TILE NEEDS TO BE ACCESIBLE
				isActivated = false;
			}
			
			if(isActivated == true) {
				arrayObjectBuilder.add("xPos", i);
				arrayObjectBuilder.add("yPos", j);
				arrayBuilder.add(arrayObjectBuilder);
			}
		}
	}
	
	//Create Json Array for enemy locations + AI
	
	JsonArrayBuilder enemyArrayBuilder = Json.createArrayBuilder();
	JsonObjectBuilder enemyArrayObject;
	for(int i = 0; i < enemies.size(); i++) {
		enemyArrayObject = Json.createObjectBuilder();
		Position currentLoc = enemies.get(i).getPos();
		enemyArrayObject.add("startingX", currentLoc.getX());
		enemyArrayObject.add("startingY", currentLoc.getY());
		enemyArrayObject.add("AI Type", "PLACEHOLDER");
		enemyArrayBuilder.add(enemyArrayObject);
	}
	
	JsonObject levelInfo = Json.createObjectBuilder()
			.add("playerX", player.getPos().getX())
			.add("playerY", player.getPos().getY())
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
	        System.out.println("File created: " + jsonFile.getName());
	      } else {
	        System.out.println("File already exists.");
	      }
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
    //Write JSON file
    try (FileWriter file = new FileWriter(jsonName)) {

        file.write(formattedString);
        file.flush();
        file.close();

    } catch (IOException e) {
        e.printStackTrace();
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

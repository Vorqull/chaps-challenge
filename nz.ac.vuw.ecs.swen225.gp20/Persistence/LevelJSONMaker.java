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
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.FreeTile;

import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class LevelJSONMaker {

  /**
   * @param ArrayFormat - The csv file converted to an ArrayList of ArrayLists.
   * @param jsonName -  The name of the JSON file to use.
   */
  public void makeJSON(ArrayList<ArrayList<String>> ArrayFormat, String jsonName) {
	int rowIndex = 0;
	int colIndex = 0;
	int playerX = 0;
	int playerY = 0;
	boolean playerSet = false;
	
	ArrayList<Position> enemyLocations = new ArrayList<Position>();
	JsonArrayBuilder colBuilder = Json.createArrayBuilder();
	for(ArrayList<String> aL : ArrayFormat) {
		colIndex = 0;
		JsonArrayBuilder rowBuilder = Json.createArrayBuilder();
		for(String s : aL) {
			String[] tileInfo = s.split(" ");
			String tileName = "ERROR!";
			String tileColour = "ERROR!";
			JsonObjectBuilder arrayObjectBuilder = Json.createObjectBuilder();
			//Set the colour of the tile if a colour was designated.
			if(tileInfo.length > 2) {
				if(tileInfo[2].equals("r")) {
					tileColour = "Red";
				}
				else if(tileInfo[2].equals("g")) {
					tileColour = "Green";
				}
				else if(tileInfo[2].equals("y")) {
					tileColour = "Yellow";
				}
				else if(tileInfo[2].equals("b")) {
					tileColour = "Blue";
				}
				else {
					arrayObjectBuilder.add("Rotation", "ERROR! value " 
											+ tileInfo[2] 
											+ " used! Can only use r b y or g! ROW: " 
											+ rowIndex
											+ " COL: "
											+ colIndex);
				}
			}
			//Set the tile type by name
			if(tileInfo[0].equals("k")) {
				tileName = "Key";
			}
			else if(tileInfo[0].equals("i")) {
				tileName = "InfoField";
			}
			else if(tileInfo[0].equals("l")) {
				tileName = "LockedDoor";
			}
			else if(tileInfo[0].equals("t")) {
				tileName = "Treasure";
			}
			else if(tileInfo[0].equals("w")) {
				tileName = "Wall";
			}
			else if(tileInfo[0].equals("f")) {
				tileName = "FreeTile";
			}
			else if(tileInfo[0].equals("el")) {
				tileName = "ExitLock";
			}
			else if(tileInfo[0].equals("ep")) {
				tileName = "ExitPortal";
			}
			//Enemy starting point, is fundamentally a freetile
			else if(tileInfo[0].equals("ene")) {
				tileName = "FreeTile";
				enemyLocations.add(new Position(colIndex, rowIndex));
			}
			//Player starting point, is fundamentally a freetile
			else if(tileInfo[0].equals("p") && playerSet == false) {
				tileName = "FreeTile";
				playerX = colIndex;
				playerY = rowIndex;
				playerSet = true;
			}
			else {
				arrayObjectBuilder.add("Rotation", "ERROR! value " 
										+ tileInfo[0] 
										+ " used! Can only use valid tile types! ROW: " 
										+ rowIndex
										+ " COL: "
										+ colIndex);
			}
			arrayObjectBuilder.add("column", colIndex);
			arrayObjectBuilder.add("row", rowIndex);
			arrayObjectBuilder.add("Tile Type", tileName);
			if(tileInfo.length > 1) {
				if(tileInfo[1].equals("0")) {
					arrayObjectBuilder.add("Rotation", "Horizontal");
				}
				else if (tileInfo[1].equals("1")) {
					arrayObjectBuilder.add("Rotation", "Vertical");
				}
				else {
					arrayObjectBuilder.add("Rotation", "ERROR! value " 
											+ tileInfo[1] 
											+ " used! Can only use 1 or 0 for rotation! ROW: " 
											+ rowIndex
											+ " COL: "
											+ colIndex);
				}
			}
			else {
				arrayObjectBuilder.add("Rotation", "Horizontal");
			}
			if(tileInfo.length == 3) {
				arrayObjectBuilder.add("Colour", tileColour);
			}
			if(tileInfo[0].equals("i")) {
				arrayObjectBuilder.add("InfoText", "Placeholder text");
			}
			JsonObject arrayObject = arrayObjectBuilder.build();
			rowBuilder.add(arrayObject);
			colIndex++;
		}
		
		JsonObject arrayRow = Json.createObjectBuilder().add("objects", rowBuilder).build();
		colBuilder.add(arrayRow);
		rowIndex++;
	}
	
	//Create Json Array for enemy locations + AI
	
	JsonArrayBuilder enemyArrayBuilder = Json.createArrayBuilder();
	JsonObjectBuilder enemyArrayObject;
	for(int i = 0; i < enemyLocations.size(); i++) {
		enemyArrayObject = Json.createObjectBuilder();
		Position currentLoc = enemyLocations.get(i);
		enemyArrayObject.add("startingX", currentLoc.getX());
		enemyArrayObject.add("startingY", currentLoc.getY());
		enemyArrayObject.add("AI Type", "PLACEHOLDER - INSERT AI TYPE (EG: PatternEnemy, StalkerEnemy) INSERT RATE OF MOVEMENT BELOW");
		enemyArrayObject.add("Tick Speed", 1);
		enemyArrayObject.add("Movement String", "PLACEHOLDER - USED FOR MOVEMENT OF PATTERNENEMY. FORMAT IS wasd eg: wassawdws");
		enemyArrayBuilder.add(enemyArrayObject);
	}
	
	JsonObject arrayCol = Json.createObjectBuilder()
			.add("rows", colBuilder)
			.build();
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

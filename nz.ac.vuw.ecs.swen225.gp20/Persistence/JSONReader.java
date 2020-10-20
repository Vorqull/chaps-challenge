package Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import Maze.Position;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.ExitLock;
import Maze.BoardObjects.Tiles.ExitPortal;
import Maze.BoardObjects.Tiles.FreeTile;
import Maze.BoardObjects.Tiles.InfoField;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.LockedDoor;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.BoardObjects.Tiles.Wall;

public class JSONReader {

  /**
   * @param jsonName -  The name of the JSON file to use.
   */
  public Level readJSON(String jsonName) {
	Level returnLevel;
	InputStream levelInputStream;
	 System.out.println("Working Directory = " + System.getProperty("user.dir"));
	try {
		levelInputStream = new FileInputStream(jsonName);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	JsonReader levelReader = Json.createReader(levelInputStream);
	
	JsonObject fileObject = levelReader.readObject();
	JsonObject levelInfo = fileObject.getJsonObject("Level Info");
	JsonObject tiles = fileObject.getJsonObject("Tiles");
	JsonArray rows = (JsonArray) tiles.get("rows");
	
	int rowCount = levelInfo.getInt("rowCount");
	int colCount = levelInfo.getInt("columnCount");
	int maxTime = levelInfo.getInt("timeLimit");
	Position playerStart = new Position(levelInfo.getInt("playerX"), levelInfo.getInt("playerY"));
	
	AbstractTile[][] tileArray = new AbstractTile[colCount][rowCount];
	
	Iterator<JsonValue> rowsIterator = rows.iterator();
	
	while(rowsIterator.hasNext()) {
		//Convert from jsonValue to jsonObject, then get the array of tiles
		JsonObject currentRowObject = (JsonObject) rowsIterator.next();
		JsonArray currentRow = (JsonArray) currentRowObject.get("objects");
		
		//Iterate through each row of tiles
		Iterator<JsonValue> currentRowIterator = currentRow.iterator();
		while(currentRowIterator.hasNext()) {
			JsonObject currentTile = (JsonObject) currentRowIterator.next();
			JsonValue type = currentTile.get("Tile Type");
			JsonValue row = currentTile.get("row");
			JsonValue column = currentTile.get("column");
			JsonValue rotated = currentTile.get("Rotation");
			boolean isRotated = false;
			if(rotated.toString().equals("Horizontal")) {
				isRotated = false;
			}
			else if(rotated.toString().equals("Vertical")) {
				isRotated = true;
			}
			String tileName = type.toString();
			int tileRow = StringToInt(row.toString());
			int tileColumn = StringToInt(column.toString());
			AbstractTile tileObject;
			if(tileName.equals("\"Key\"")) {
				JsonValue colour = currentTile.get("Colour");
				String tileColour = colour.toString();
				tileColour = tileColour.substring(1, tileColour.length()-1);
				tileObject = new Key(tileColour);
			}
			else if(tileName.equals("\"ExitPortal\"")) {
				tileObject = new ExitPortal();
			}
			else if(tileName.equals("\"ExitLock\"")) {
				tileObject = new ExitLock(isRotated);
			}
			else if(tileName.equals("\"InfoField\"")) {
				JsonValue infoText = currentTile.get("InfoText");
				String tileInfoText = infoText.toString();
				tileObject = new InfoField(tileInfoText);
			}
			else if(tileName.equals("\"LockedDoor\"")) {
				JsonValue colour = currentTile.get("Colour");
				String tileColour = colour.toString();
				tileColour = tileColour.substring(1, tileColour.length()-1);
				tileObject = new LockedDoor(isRotated, tileColour);
			}
			else if(tileName.equals("\"Treasure\"")) {
				tileObject = new Treasure();
			}
			else if(tileName.equals("\"Wall\"")) {
				tileObject = new Wall();
			}
			//Free tile
			else{
				tileObject = new FreeTile();
			}
			tileArray[tileColumn][tileRow] = tileObject;
		}
	}
	returnLevel = new Level(maxTime, playerStart, tileArray, new EnemyBlueprint[10]);
	return returnLevel;
	
  
  }

  private int StringToInt(String intString) {
	  int charInt = 0;
	  for(int i = 0; i < intString.length(); i++) {
		  charInt = charInt*10;
		  charInt += intString.charAt(i) - '0';
	  }
	  return charInt;
  }
}

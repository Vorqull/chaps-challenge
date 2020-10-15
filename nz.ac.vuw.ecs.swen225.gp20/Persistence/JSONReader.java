package Persistence;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

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

import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class JSONReader {

  /**
   * @param jsonName -  The name of the JSON file to use.
   */
  public AbstractTile[][] readJSON(String jsonName) {
	int rowIndex = 0;
	int colIndex = 0;
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
	JsonObject tiles = fileObject.getJsonObject("Tiles");
	JsonArray rows = (JsonArray) tiles.get("rows");
	int rowCount = fileObject.getInt("rowCount");
	int colCount = fileObject.getInt("columnCount");
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
			int tileRow = row.toString().charAt(0);
			int tileColumn = column.toString().charAt(0);
			tileRow = tileRow - '0';
			tileColumn = tileColumn - '0';
			Position tilePos = new Position(tileColumn, tileRow);
			System.out.println(tilePos.getX() + " " + tilePos.getY());
			AbstractTile tileObject;
			if(tileName.equals("\"Key\"")) {
				JsonValue colour = currentTile.get("Colour");
				String tileColour = colour.toString();
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
	
	return tileArray;
	
  
  }
}

package Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import Maze.Position;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.ExitLock;
import Maze.BoardObjects.Tiles.ExitPortal;
import Maze.BoardObjects.Tiles.FreeTile;
import Maze.BoardObjects.Tiles.InfoField;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.LockedDoor;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.BoardObjects.Tiles.Wall;

public class SaveJSONReader {

  /**
   * @param jsonName -  The name of the JSON file to use.
   */
  public static Level readJSON(String jsonName, Level loadedLevel) {
	ArrayList<EnemyBlueprint> enemyBlueprintsNew = new ArrayList<EnemyBlueprint>();
	
	AbstractTile[][] tileArray = loadedLevel.getTileArray();
	Level returnLevel;
	
	InputStream levelInputStream;
	 System.out.println("Working Directory = " + System.getProperty("user.dir"));
	try {
		levelInputStream = new FileInputStream(jsonName);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		return null;
	}
	JsonReader saveReader = Json.createReader(levelInputStream);
	
	JsonObject fileObject = saveReader.readObject();
	JsonObject gameplayVariables = fileObject.getJsonObject("Gameplay variables");
	JsonArray changedTiles = (JsonArray) fileObject.get("Changed Tiles");
	JsonArray enemyLocations = (JsonArray) gameplayVariables.get("Enemy locations");
	
	int startingTime = gameplayVariables.getInt("time remaining");
	Position playerStart = new Position(gameplayVariables.getInt("playerX"), gameplayVariables.getInt("playerY"));
	
	
	
	Iterator<JsonValue> tilesIterator = changedTiles.iterator();
	
	//Utility player object with all keys for unlocking doors
	Player utilityPlayer = new Player(new Position(0,0));
	
	while(tilesIterator.hasNext()) {
		
		JsonObject currentTileJSON = (JsonObject) tilesIterator.next();
		int tileX = currentTileJSON.getInt("xPos");
		int tileY = currentTileJSON.getInt("yPos");
		AbstractTile currentTileObject;
		currentTileObject = tileArray[tileX][tileY];
		if(currentTileObject instanceof ExitLock) {
			ExitLock castTile = (ExitLock) currentTileObject;
			castTile.unlock();
		}
		else if(currentTileObject instanceof Treasure) {
			Treasure castTile = (Treasure) currentTileObject;
			castTile.interact(utilityPlayer);
		}
		else if(currentTileObject instanceof LockedDoor) {
			LockedDoor castTile = (LockedDoor) currentTileObject;
			castTile.interact(utilityPlayer);
		}
		else if(currentTileObject instanceof Key) {
			Key castTile = (Key) currentTileObject;
			castTile.interact(utilityPlayer);
		}
		
	}
	
	//Iterate through all the enemies
	Iterator<JsonValue> enemiesIterator = enemyLocations.iterator();
	//With each enemy, get its new position and retrieve the AI type to ensure it is the same type of enemy as when saved
	while(enemiesIterator.hasNext()) {	
		JsonObject currentEnemyJSON = (JsonObject) enemiesIterator.next();
		int enemyX = currentEnemyJSON.getInt("startingX");
		int enemyY = currentEnemyJSON.getInt("startingY");
		String enemyAI = currentEnemyJSON.getString("AI Type");
		EnemyBlueprint currentEnemyBlueprint = new EnemyBlueprint(new Position(enemyX, enemyY), enemyAI);
		enemyBlueprintsNew.add(currentEnemyBlueprint);
	}
	
	//Make a revised level object that accounts for new enemy and player positions
	//as well as changed tile state.
	returnLevel = new Level(startingTime, playerStart, tileArray, enemyBlueprintsNew);
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

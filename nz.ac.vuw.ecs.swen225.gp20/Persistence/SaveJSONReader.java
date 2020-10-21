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
import Maze.BoardObjects.Actors.AbstractActor;
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
	ArrayList<AbstractActor> enemiesUpdated = new ArrayList<AbstractActor>();
	
	AbstractTile[][] tileArray = loadedLevel.getTileArray();
	Level returnLevel;
	
	InputStream levelInputStream;
	 System.out.println("Working Directory = " + System.getProperty("user.dir"));
	try {
		levelInputStream = new FileInputStream(jsonName);
	} catch (FileNotFoundException e) {
		return null;
	}
	JsonReader saveReader = Json.createReader(levelInputStream);
	
	JsonObject fileObject = saveReader.readObject();
	JsonObject gameplayVariables = fileObject.getJsonObject("Gameplay variables");
	JsonArray changedTiles = (JsonArray) fileObject.get("Changed Tiles");
	JsonArray keysInHand = (JsonArray) gameplayVariables.get("Keys on hand");
	JsonArray treasureInHand = (JsonArray) gameplayVariables.get("Treasure on hand");
	JsonArray enemyLocations = (JsonArray) gameplayVariables.get("Enemy locations");
	
	int startingTime = gameplayVariables.getInt("time remaining");
	Position playerStart = new Position(gameplayVariables.getInt("playerX"), gameplayVariables.getInt("playerY"));
	
	
	
	Iterator<JsonValue> tilesIterator = changedTiles.iterator();
	
	
	while(tilesIterator.hasNext()) {	
		JsonObject currentTileJSON = (JsonObject) tilesIterator.next();
		int tileX = currentTileJSON.getInt("xPos");
		int tileY = currentTileJSON.getInt("yPos");
		AbstractTile currentTileObject;
		currentTileObject = tileArray[tileX][tileY];
		if(currentTileObject.isChanged()) {
			currentTileObject.setChangedTile();
		}
		
	}
	
	//Iterate through all the enemies
	Iterator<JsonValue> enemiesIterator = enemyLocations.iterator();
	//With each enemy, get its new position and retrieve the AI type to ensure it is the same type of enemy as when saved
	while(enemiesIterator.hasNext()) {	
		JsonObject currentEnemyJSON = (JsonObject) enemiesIterator.next();
		int startingX = currentEnemyJSON.getInt("startingX");
		int startingY = currentEnemyJSON.getInt("startingY");
		int newX = currentEnemyJSON.getInt("startingX");
		int newY = currentEnemyJSON.getInt("startingX");
		
		Position newPos = new Position(newX, newY);
		Position startingPos = new Position(startingX, startingY);
		
		AbstractActor currentEnemy = findEnemy(startingPos, loadedLevel.getEnemies());
		
		if(currentEnemy == null) {
			System.out.println("ORIGINAL ENEMY NOT FOUND");
		}
		else {
			currentEnemy.setPos(newPos);
		}
	}
	
	//Iterate through all keys in hand
	Iterator<JsonValue> keysIterator = keysInHand.iterator();
	//With key, 
	while(keysIterator.hasNext()) {	
		JsonObject currentKeyJSON = (JsonObject) keysIterator.next();
		int keyX = currentKeyJSON.getInt("xPos");
		int keyY = currentKeyJSON.getInt("yPos");
		if(tileArray[keyX][keyY] instanceof Key) {
			Key keyObject = (Key) tileArray[keyX][keyY];
			loadedLevel.getPlayer().pickupKey(keyObject);
		}
		else {
			System.out.println("ERROR - KEY IN HAND NOT LOADED ON MAP");
		}
	}
	
	//Iterate through all treasure in hand
	Iterator<JsonValue> treasureIterator = treasureInHand.iterator();
	//With key, 
	while(keysIterator.hasNext()) {	
		JsonObject currentTreasureJSON = (JsonObject) treasureIterator.next();
		int treasureX = currentTreasureJSON.getInt("xPos");
		int treasureY = currentTreasureJSON.getInt("yPos");
		if(tileArray[treasureX][treasureY] instanceof Treasure) {
			Treasure treasureObject = (Treasure) tileArray[treasureX][treasureY];
			loadedLevel.getPlayer().pickupTreasure(treasureObject);
		}
		else {
			System.out.println("ERROR - TREASURE IN HAND NOT LOADED ON MAP");
		}
	}
	
	//Make a revised level object that accounts for new enemy and player positions
	//as well as changed tile state.
	returnLevel = new Level(startingTime, loadedLevel.getPlayer(), tileArray, loadedLevel.getEnemies());
	return returnLevel;
  
  }

  private static AbstractActor findEnemy(Position pos, ArrayList<AbstractActor> passedArray) {
	  for(AbstractActor aA : passedArray) {
		 if(aA.getPos().getX() == pos.getX() && aA.getPos().getY() == pos.getY()){
			 return aA;
		 }
	  }
	  return null;
  }
  
  
}

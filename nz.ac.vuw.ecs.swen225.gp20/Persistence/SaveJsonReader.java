package Persistence;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * A class containing the SaveJsonReader method. 
 * 
 * @author Lukas Stanley
 *
 */
public class SaveJsonReader {

  /**
   * A method to read a save file in JSON format, 
   * and put the attributes onto an already loaded level.
   * 
   * @param jsonName - The name of the JSON file to use.
   * @param loadedLevel - The level object on which to load the save.
   * @return the passed Level object after it has been changed with the parameters of the save file.
   */
  public static Level readJson(String jsonName, Level loadedLevel) {

    AbstractTile[][] tileArray = loadedLevel.getTileArray();
    
    
    InputStream levelInputStream;
    try {
      levelInputStream = new FileInputStream(jsonName);
    } catch (FileNotFoundException e) {
      return null;
    }
    JsonReader saveReader = Json.createReader(levelInputStream);

    JsonObject fileObject = saveReader.readObject();
    JsonObject gameplayVariables = fileObject.getJsonObject("Gameplay variables");
    JsonArray changedTiles = (JsonArray) fileObject.get("Changed Tiles");
    JsonArray enemyLocations = (JsonArray) gameplayVariables.get("Enemy locations");

    Position playerStart = new Position(gameplayVariables.getInt("playerX"),
        gameplayVariables.getInt("playerY")
    );
    loadedLevel.getPlayer().setPos(playerStart);

    Iterator<JsonValue> tilesIterator = changedTiles.iterator();

    while (tilesIterator.hasNext()) {
      JsonObject currentTileJson = (JsonObject) tilesIterator.next();
      int tileX = currentTileJson.getInt("xPos");
      int tileY = currentTileJson.getInt("yPos");
      AbstractTile currentTileObject;
      currentTileObject = tileArray[tileX][tileY];
      currentTileObject.setChangedTile();

    }

    // Iterate through all the enemies
    Iterator<JsonValue> enemiesIterator = enemyLocations.iterator();
    // With each enemy, get its new position and retrieve the AI type to ensure it
    // is the same type of enemy as when saved
    while (enemiesIterator.hasNext()) {
      JsonObject currentEnemyJson = (JsonObject) enemiesIterator.next();
      int startingX = currentEnemyJson.getInt("startingX");
      int startingY = currentEnemyJson.getInt("startingY");
      int newX = currentEnemyJson.getInt("startingX");
      int newY = currentEnemyJson.getInt("startingX");

      Position newPos = new Position(newX, newY);
      Position startingPos = new Position(startingX, startingY);

      AbstractActor currentEnemy = findEnemy(startingPos, loadedLevel.getEnemies());

      if (currentEnemy == null) {
        System.out.println("ORIGINAL ENEMY NOT FOUND");
      } else {
        currentEnemy.setPos(newPos);
      }
    }
    
    JsonArray keysInHand = (JsonArray) gameplayVariables.get("Keys on hand");
    // Iterate through all keys in hand
    Iterator<JsonValue> keysIterator = keysInHand.iterator();
    // With key,
    while (keysIterator.hasNext()) {
      JsonObject currentKeyJson = (JsonObject) keysIterator.next();
      int keyX = currentKeyJson.getInt("xPos");
      int keyY = currentKeyJson.getInt("yPos");
      if (tileArray[keyX][keyY] instanceof Key) {
        Key keyObject = (Key) tileArray[keyX][keyY];
        loadedLevel.getPlayer().pickupKey(keyObject);
      } else {
        System.out.println("ERROR - KEY IN HAND NOT LOADED ON MAP");
      }
    }
    
    JsonArray treasureInHand = (JsonArray) gameplayVariables.get("Treasure on hand");
    // Iterate through all treasure in hand
    Iterator<JsonValue> treasureIterator = treasureInHand.iterator();
    // With key,
    while (keysIterator.hasNext()) {
      JsonObject currentTreasureJson = (JsonObject) treasureIterator.next();
      int treasureX = currentTreasureJson.getInt("xPos");
      int treasureY = currentTreasureJson.getInt("yPos");
      if (tileArray[treasureX][treasureY] instanceof Treasure) {
        Treasure treasureObject = (Treasure) tileArray[treasureX][treasureY];
        loadedLevel.getPlayer().pickupTreasure(treasureObject);
      } else {
        System.out.println("ERROR - TREASURE IN HAND NOT LOADED ON MAP");
      }
    }
    
    int startingTime = gameplayVariables.getInt("time remaining");
    
    // Make a revised level object that accounts for new enemy and player positions
    // as well as changed tile state.
    Level returnLevel = new Level(
        startingTime,
        loadedLevel.getPlayer(),
        tileArray,
        loadedLevel.getEnemies()
    );
    return returnLevel;

  }

  private static AbstractActor findEnemy(Position pos, Set<AbstractActor> passedSet) {
    for (AbstractActor currentActor : passedSet) {
      if (currentActor.getPos().getX() == pos.getX() 
          && currentActor.getPos().getY() == pos.getY()
          ) {
        return currentActor;
      }
    }
    return null;
  }

}

package Persistence;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Actors.StalkerEnemy;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.DeathTile;
import Maze.BoardObjects.Tiles.ExitLock;
import Maze.BoardObjects.Tiles.ExitPortal;
import Maze.BoardObjects.Tiles.FreeTile;
import Maze.BoardObjects.Tiles.InfoField;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.LockedDoor;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.BoardObjects.Tiles.Wall;
import Maze.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * Utility class for loading a level JSON file and converting it to a Level class.
 * 
 * @author Lukas Stanley
 *
 */
public class LevelJsonReader {

  /**
   * Loads a level JSON file and returns it as a Level class to be loaded by the maze.
   * 
   * @param jsonName - The name of the JSON file to use.
   * @return The Level file as per read from the JSON file.
   */
  public static Level readJson(String jsonName) {

    InputStream levelInputStream;
    try {
      levelInputStream = new FileInputStream(jsonName);
    } catch (FileNotFoundException e) {
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
    JsonArray enemies = (JsonArray) levelInfo.get("Enemies");
    Position playerStart = new Position(levelInfo.getInt("playerX"), levelInfo.getInt("playerY"));

    AbstractTile[][] tileArray = new AbstractTile[colCount][rowCount];

    Iterator<JsonValue> rowsIterator = rows.iterator();

    while (rowsIterator.hasNext()) {
      // Convert from jsonValue to jsonObject, then get the array of tiles
      JsonObject currentRowObject = (JsonObject) rowsIterator.next();
      JsonArray currentRow = (JsonArray) currentRowObject.get("objects");

      // Iterate through each row of tiles
      Iterator<JsonValue> currentRowIterator = currentRow.iterator();
      while (currentRowIterator.hasNext()) {
        JsonObject currentTile = (JsonObject) currentRowIterator.next();
        JsonValue type = currentTile.get("Tile Type");
        JsonValue row = currentTile.get("row");
        JsonValue column = currentTile.get("column");
        JsonValue rotated = currentTile.get("Rotation");
        boolean isRotated = false;
        if (rotated.toString().equals("\"Horizontal\"")) {
          isRotated = false;
        } else if (rotated.toString().equals("\"Vertical\"")) {
          isRotated = true;
        }
        String tileName = type.toString();
        int tileRow = stringToInt(row.toString());
        int tileColumn = stringToInt(column.toString());
        AbstractTile tileObject;
        if (tileName.equals("\"Key\"")) {
          JsonValue colour = currentTile.get("Colour");
          String tileColour = colour.toString();
          tileColour = tileColour.substring(1, tileColour.length() - 1);
          tileObject = new Key(tileColour);
        } else if (tileName.equals("\"ExitPortal\"")) {
          tileObject = new ExitPortal();
        } else if (tileName.equals("\"ExitLock\"")) {
          tileObject = new ExitLock(isRotated);
        } else if (tileName.equals("\"InfoField\"")) {
          JsonValue infoText = currentTile.get("InfoText");
          String tileInfoText = infoText.toString();
          tileObject = new InfoField(tileInfoText);
        } else if (tileName.equals("\"LockedDoor\"")) {
          JsonValue colour = currentTile.get("Colour");
          String tileColour = colour.toString();
          tileColour = tileColour.substring(1, tileColour.length() - 1);
          tileObject = new LockedDoor(isRotated, tileColour);
        } else if (tileName.equals("\"Treasure\"")) {
          tileObject = new Treasure();
        } else if (tileName.equals("\"DeathTile\"")) {
          tileObject = new DeathTile();
        } else if (tileName.equals("\"Wall\"")) {
          tileObject = new Wall();
          // Free tile
        } else {
          tileObject = new FreeTile();
        }
        tileArray[tileColumn][tileRow] = tileObject;
      }
    }

    ArrayList<AbstractActor> enemiesArrayList = new ArrayList<AbstractActor>();

    Iterator<JsonValue> enemiesIterator = enemies.iterator();
    JsonObject currentEnemyObject;

    while (enemiesIterator.hasNext()) {
      AbstractActor currentEnemy;
      currentEnemyObject = (JsonObject) enemiesIterator.next();
      int xstartPos = currentEnemyObject.getInt("startingX");
      int ystartPos = currentEnemyObject.getInt("startingY");
      JsonValue aiType = currentEnemyObject.get("AI Type");
      int tickSpeed = currentEnemyObject.getInt("Tick Speed");
      JsonValue movement = currentEnemyObject.get("Movement String");

      Position aiStartPos = new Position(xstartPos, ystartPos);

      String aiTypeString = aiType.toString();
      String movementString = movement.toString();
      aiTypeString = aiTypeString.substring(1, aiTypeString.length() - 1);
      movementString = movementString.substring(1, movementString.length() - 1);

      if (aiTypeString.equals("PatternEnemy")) {
        currentEnemy = new PatternEnemy(aiStartPos, tickSpeed, movementString);
        enemiesArrayList.add(currentEnemy);
      } else if (aiTypeString.equals("StalkerEnemy")) {
        currentEnemy = new StalkerEnemy(aiStartPos, tickSpeed);
        enemiesArrayList.add(currentEnemy);
      }

    }

    Player returnPlayer = new Player(playerStart);
    Set<AbstractActor> returnEnemies = new HashSet<AbstractActor>();
    returnEnemies.addAll(enemiesArrayList);
    int maxTime = levelInfo.getInt("timeLimit");
    Level returnLevel = new Level(maxTime, returnPlayer, tileArray, returnEnemies);
    return returnLevel;

  }

  private static int stringToInt(String intString) {
    int charInt = 0;
    for (int i = 0; i < intString.length(); i++) {
      charInt = charInt * 10;
      charInt += intString.charAt(i) - '0';
    }
    return charInt;
  }
}

package Persistence;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;


/**
 * The main module of Persistence. 
 * Base class for calling all functions of Persistence.
 *
 * @author Lukas Stanley
 *
 */
public class Persistence {
  /**
   * Get a level by specifying which number it is. 
   * Json file is loaded into a Level accordingly and returned.
   * 
   * @param levelNumber Which level to load.
   * @return The JSON file loaded in Level format.
   */
  public static Level getLevel(int levelNumber) {
    String levelString = "levels/level" + levelNumber + ".JSON";
    Level returnLevel = LevelJsonReader.readJson(levelString);
    return returnLevel;
  }

  /**
   * Save a level's current state for later loading.
   * 
   * @param remainingTime The remaining time to save.
   * @param player The player which occupies the level as it is saved.
   * @param set The enemies of the current level in an ArrayList format.
   * @param levelNumber The level number which is being saved.
   * @param tiles The 2d array of tiles which comprise the level.
   * @return whether or not the save was successful.
   */
  public static boolean saveGame(
      int remainingTime, 
      Player player, 
      Set<AbstractActor> set, 
      int levelNumber,
      AbstractTile[][] tiles
  ) {
    String saveString = "saves/level" + levelNumber + ".JSON";
    SaveJsonMaker.makeJson(remainingTime, player, set, saveString, tiles);
    return true;
  }

  /**
   * Load a level in a given save state.
   * 
   * @param levelNumber which level to load in it's saved state.
   * @return the Level object, which has been re-loaded and then converted to the saved state.
   */
  public static Level loadGame(int levelNumber) {
    String saveString = "saves/level" + levelNumber + ".JSON";
    Level levelUnchanged = getLevel(levelNumber);
    Level levelLoaded = SaveJsonReader.readJson(saveString, levelUnchanged);
    // Can return as null
    return levelLoaded;
  }

  /**
   * Erase the save of a given level.
   * 
   * @param levelNumber which level's save to erase.
   * @return whether or not the save was successfully erased.
   */
  public static boolean eraseSave(int levelNumber) {
    String saveString = "saves/level" + levelNumber + ".JSON";
    try {
      File f = new File(saveString); // file to be delete
      if (f.delete()) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }
}

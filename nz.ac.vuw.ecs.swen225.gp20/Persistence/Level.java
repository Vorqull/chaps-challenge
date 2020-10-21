package Persistence;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import java.util.ArrayList;


/**
 * Storage class for passing the multiple parameter types from loading a level file.
 * 
 * @author Lukas Stanley
 *
 */
public class Level {


  private AbstractTile[][] levelArray;
  private Player player;
  private int time;
  private ArrayList<AbstractActor> enemies;

  /**
   * Level Constructor.
   * 
   * @param passedTime the starting time for the level. 
   *        Can be less than the level normally has if loaded from a save.
   * @param passedPlayer the player for the level.
   * @param passedArray the array of tiles which make up the level.
   * @param passedEnemies the ArrayList of enemies which populate the level.
   */
  public Level(
      int passedTime,
      Player passedPlayer,
      AbstractTile[][] passedArray,
      ArrayList<AbstractActor> passedEnemies
  ) {
    time = passedTime;
    levelArray = passedArray;
    player = passedPlayer;
    enemies = passedEnemies;
  }

  /**
   * Get the base time for the current level.
   * 
   * @return the base time for the given level.
   */
  public int getTime() {
    return time;
  }

  /**
   * Get the player object being used in the current level.
   * 
   * @return the player in the current level.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Get the entire level's worth of tiles.
   * 
   * @return the entire level's worth of tiles in AbstractTile[][] form.
   */
  public AbstractTile[][] getTileArray() {
    return levelArray;
  }

  /**
   * Get all the enemies in the current level.
   * 
   * @return ArrayList of all enemies in AbstractActor form.
   */
  public ArrayList<AbstractActor> getEnemies() {
    return enemies;
  }
}
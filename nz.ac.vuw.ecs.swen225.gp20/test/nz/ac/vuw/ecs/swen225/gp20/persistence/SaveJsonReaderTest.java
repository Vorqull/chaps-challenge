package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

import Maze.Position;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Persistence.Level;
import Persistence.Persistence;

/**
 * @author Admin
 *
 */
public class SaveJsonReaderTest {

  /**
   * Try to read Level 1's save. Creates the save first with treasure.
   */
  @Test
  public void testReadJson1() {
    //Loads the level in order to create a new save.
    Level testLevel = Persistence.getLevel(1);
    
    //Change the first treasure found in the level.
    boolean broken = false;
    Treasure testTreasure = null;
    Position testTreasurePos = null;
    for (int i = 0; i < testLevel.getTileArray().length; i++) {
      for (int j = 0; j < testLevel.getTileArray()[i].length; j++) {
        if (testLevel.getTileArray()[i][j] != null) {
          if (testLevel.getTileArray()[i][j] instanceof Treasure) {
            //Find a treasure object and change it.
            testTreasure = (Treasure) testLevel.getTileArray()[i][j];
            testTreasure.setChangedTile();
            testTreasurePos = new Position(i, j);
            broken = true;
          }
        }
        
        if (broken == true) {
          break;
        }
      }
      if (broken == true) {
        break;
      }
    }
    
    //The level should ALWAYS have treasure. Likely an issue.
    assert (testTreasure != null);
    
    //Save the game, having changed a treasure.
    Persistence.saveGame(
        testLevel.getTime(),
        testLevel.getPlayer(),
        testLevel.getEnemies(),
        1,
        testLevel.getTileArray()
    );
    Level testResult = Persistence.loadGame(1);
    
    //having changed an object initially, see if this change was saved
    boolean testResultChanged = testResult.getTileArray()
        [testTreasurePos.getX()]
            [testTreasurePos.getY()].isChanged();
    assertEquals(testTreasure.isChanged(), testResultChanged);
  }
  
  /**
   * Try to read Level 2's save. Creates the save first with key.
   */
  @Test
  public void testReadJson2() {
    //Loads the level in order to create a new save.
    Level testLevel = Persistence.getLevel(2);
    
    //Change the first key found in the level.
    boolean broken = false;
    Key testKey = null;
    Position testKeyPos = null;
    for (int i = 0; i < testLevel.getTileArray().length; i++) {
      for (int j = 0; j < testLevel.getTileArray()[i].length; j++) {
        if (testLevel.getTileArray()[i][j] != null) {
          if (testLevel.getTileArray()[i][j] instanceof Key) {
            //Find a treasure object and change it.
            testKey = (Key) testLevel.getTileArray()[i][j];
            testKeyPos = new Position(i, j);
            testLevel.getPlayer().pickupKey(testKey);
            broken = true;
          }
        }
        
        if (broken == true) {
          break;
        }
      }
      if (broken == true) {
        break;
      }
    }
    
    //The second level should ALWAYS have at least 1 key.
    assert (testKey != null);
    
    //Save the game, having changed a treasure.
    Persistence.saveGame(
        testLevel.getTime(),
        testLevel.getPlayer(),
        testLevel.getEnemies(),
        2,
        testLevel.getTileArray()
    );
    Level testResult = Persistence.loadGame(2);
    
    //having changed an object initially, see if this change was saved
    boolean testResultChanged = testResult.getTileArray()
        [testKeyPos.getX()]
            [testKeyPos.getY()].isChanged();
    assertEquals(testKey.isChanged(), testResultChanged);
  }
  
  /**
   * Try to read Level 1's save after deleting it.
   */
  @Test
  public void testReadJson3() {
    //Load level 1.
    Level testLevel = Persistence.getLevel(1);
    
    //Save the game, having changed nothing.
    Persistence.saveGame(
        testLevel.getTime(),
        testLevel.getPlayer(),
        testLevel.getEnemies(),
        1,
        testLevel.getTileArray()
    );
    
    //Erase the save, now that it has been overwritten/created.
    Persistence.eraseSave(1);
    
    //try to load the save
    Level testResult = Persistence.loadGame(1);
    
    //The return value is not meant to be null.
    assert (testResult != null);
  }
  

}

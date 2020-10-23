package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Tiles.InfoField;
import Persistence.Level;
import Persistence.Persistence;
import org.junit.Test;

/**
 * Junit test cases for LevelJsonReader.
 * 
 * @author Lukas Stanley
 *
 */
public class LevelJsonReaderTest {

  /**
   * Tests that level 1 can be loaded from JSON.
   */
  @Test
  public void testMakeJson1() {
    Level testResult = Persistence.getLevel(1);
    assertEquals(InfoField.class, testResult.getTileArray()[8][6].getClass());
  }

  /**
   * Tests that enemies be loaded from JSON, and that level 2 can be loaded.
   */
  @Test
  public void testMakeJson2() {
    Level testResult = Persistence.getLevel(2);
    assertEquals(4, testResult.getEnemies().size());
  }
  
}

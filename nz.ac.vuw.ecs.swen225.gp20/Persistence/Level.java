package Persistence;
import Maze.Position;
import Maze.BoardObjects.Tiles.AbstractTile;


public class Level {

  /**
   * @param jsonName -  The name of the JSON file to use.
   */
	private AbstractTile[][] levelArray;
	private Position playerStartPos;
	private int time;
	private EnemyBlueprint[] enemies;
	
	public Level(int pTime, Position pPos, AbstractTile[][] pArray, EnemyBlueprint[] pEnemies) {
		time = pTime;
		levelArray = pArray;
		playerStartPos = pPos;
		enemies = pEnemies;
	}

	public int getTime() {
		return time;
	}
	
	public Position getPlayerPos() {
		return playerStartPos;
	}
	
	public AbstractTile[][] getTileArray(){
		return levelArray;
	}
	
	public EnemyBlueprint[] getEnemies() {
		return enemies;
	}
}
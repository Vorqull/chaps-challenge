package Persistence;
import java.util.ArrayList;

import Maze.Position;
import Maze.BoardObjects.Tiles.AbstractTile;


public class Level {

  /**
   * @param jsonName -  The name of the JSON file to use.
   */
	private AbstractTile[][] levelArray;
	private Position playerStartPos;
	private int time;
	private ArrayList<EnemyBlueprint> enemies;
	
	public Level(int pTime, Position pPos, AbstractTile[][] pArray, ArrayList<EnemyBlueprint> pEnemies) {
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
	
	public ArrayList<EnemyBlueprint> getEnemies() {
		return enemies;
	}
}
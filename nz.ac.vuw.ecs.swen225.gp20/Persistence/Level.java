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
	
	public Level(int pTime, Position pPos, AbstractTile[][] pArray) {
		time = pTime;
		levelArray = pArray;
		playerStartPos = pPos;
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
}
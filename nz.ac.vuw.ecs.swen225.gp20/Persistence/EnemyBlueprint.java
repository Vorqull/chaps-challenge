package Persistence;
import Maze.Position;
import Maze.BoardObjects.Tiles.AbstractTile;


public class EnemyBlueprint {

  /**
   * @param jsonName -  The name of the JSON file to use.
   */
	private String behaviourType;
	private Position startPos;
	
	public EnemyBlueprint(Position pStartPos, String pBehaviourType) {
		startPos = pStartPos;
		behaviourType = pBehaviourType;
	}
	
	public Position getPos() {
		return startPos;
	}
	
	public String getBehaviour(){
		return behaviourType;
	}
}
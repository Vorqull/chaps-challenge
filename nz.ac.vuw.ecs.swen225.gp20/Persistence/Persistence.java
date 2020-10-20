package Persistence;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;

public class Persistence {
	public static Level getLevel(int levelNumber){
		String levelString = "levels/level" + levelNumber + ".JSON";
		JSONReader readJSON = new JSONReader();
		Level returnLevel = readJSON.readJSON(levelString);
		return returnLevel;
	}
	
	public boolean saveGame(int remainingTime, Player player, AbstractActor[] enemies) {
		
		return false;
	}
}

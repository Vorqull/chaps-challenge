package Persistence;

import Maze.BoardObjects.Tiles.AbstractTile;

public class Persistence {
	public AbstractTile[][] getLevel(int levelNumber){
		String levelString = "levels/level" + levelNumber + ".JSON";
		JSONReader readJSON = new JSONReader();
		AbstractTile[][] returnSet = readJSON.readJSON(levelString);
		return returnSet;
	}
}

package Persistence;

import Maze.BoardObjects.Tiles.AbstractTile;

public class PersistenceTest {
	public static void main(String[] args) {
	    CSVReader csvRead = new CSVReader();
	    JSONMaker makeJSON = new JSONMaker();
	    Persistence getJson = new Persistence();
	    makeJSON.makeJSON(csvRead.readCSV("nz.ac.vuw.ecs.swen225.gp20/Persistence/level.csv"), "nz.ac.vuw.ecs.swen225.gp20/Persistence/level.json");
	    
	    Level testLevel = getJson.getLevel(1);
	    AbstractTile[][] tileTest = testLevel.getTileArray();
	    String className = tileTest[10][10].getClass().getName();
	    System.out.println("Level Info:" + testLevel.getTime() + " " + testLevel.getPlayerPos());
	}
}

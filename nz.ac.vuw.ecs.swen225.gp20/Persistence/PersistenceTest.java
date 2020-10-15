package Persistence;

import Maze.BoardObjects.Tiles.AbstractTile;

public class PersistenceTest {
	public static void main(String[] args) {
	    CSVReader csvRead = new CSVReader();
	    JSONMaker makeJSON = new JSONMaker();
	    Persistence getJson = new Persistence();
	    makeJSON.makeJSON(csvRead.readCSV("nz.ac.vuw.ecs.swen225.gp20/Persistence/level.csv"), "nz.ac.vuw.ecs.swen225.gp20/Persistence/level.json");
	    
	    AbstractTile[][] tileTest = getJson.getLevel(1);
	    String className = tileTest[0][0].getClass().getName();
	    System.out.println("First abstract tile: " + className);
	}
}

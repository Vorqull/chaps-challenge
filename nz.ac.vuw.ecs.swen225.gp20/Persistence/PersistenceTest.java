package Persistence;

import java.util.ArrayList;
import Maze.Position;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.PatternEnemy;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;

public class PersistenceTest {
	public static void main(String[] args) {
	    CSVReader csvRead = new CSVReader();
	    LevelJSONMaker makeJSON = new LevelJSONMaker();
	    Persistence getJson = new Persistence();
	    makeJSON.makeJSON(csvRead.readCSV("nz.ac.vuw.ecs.swen225.gp20/Persistence/level.csv"), "nz.ac.vuw.ecs.swen225.gp20/Persistence/level.json");
	    
	    Level testLevel = getJson.getLevel(1);
	    AbstractTile[][] tileTest = testLevel.getTileArray();
	    String className = tileTest[10][10].getClass().getName();
	    System.out.println("Enemies " + testLevel.getEnemies().get(0).getPos());
	    
	    ArrayList<AbstractActor> actorArray = new ArrayList<AbstractActor>();
	    //actorArray.add(new PatternEnemy(new Position(2, 6), "GRIMBA"));
	    
	    
	    getJson.saveGame(91, new Player(new Position(3, 4)), actorArray, 1, tileTest);
	    
	   Level savedLevelLoad = getJson.loadGame(1);
	   Treasure testTile = (Treasure) savedLevelLoad.getTileArray()[3][6];
	   System.out.println(testTile.isPickedUp());
	   
	   PatternEnemy actorMake;
	   Position tempPos = new Position(6, 7);
	   new PatternEnemy(tempPos, "yes");
	}
}
